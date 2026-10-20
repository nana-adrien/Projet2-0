package empire.digiprem.projet2_0.server.deuxzero.service

import empire.digiprem.projet2_0.contracts.deuxzero.CampDto
import empire.digiprem.projet2_0.contracts.deuxzero.CreateDeuxZeroRequest
import empire.digiprem.projet2_0.contracts.deuxzero.DeuxZeroDto
import empire.digiprem.projet2_0.contracts.deuxzero.EngageClubRequest
import empire.digiprem.projet2_0.contracts.deuxzero.NommerCapitaineRequest
import empire.digiprem.projet2_0.contracts.deuxzero.UpdateDeuxZeroSettingsRequest
import empire.digiprem.projet2_0.contracts.validation.validateCreateDeuxZeroRequest
import empire.digiprem.projet2_0.contracts.validation.validateEngageClubRequest
import empire.digiprem.projet2_0.server.clubs.infra.repository.AdhesionClubRepository
import empire.digiprem.projet2_0.server.clubs.infra.repository.ClubRepository
import empire.digiprem.projet2_0.server.common.exception.ConflictException
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.common.util.orThrow
import empire.digiprem.projet2_0.server.deuxzero.infra.entity.CampEntity
import empire.digiprem.projet2_0.server.deuxzero.infra.entity.DeuxZeroEntity
import empire.digiprem.projet2_0.server.deuxzero.infra.entity.SaisonEntity
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.CampRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.DeuxZeroRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.EffectifEngageRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.SaisonRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import kotlin.random.Random

private const val ROLE_ADMIN = "ADMINISTRATEUR"
private const val STATUT_ACCEPTEE = "ACCEPTEE"
private const val STATUT_EN_ATTENTE_ENGAGEMENT = "EN_ATTENTE_ENGAGEMENT"
private const val STATUT_ACTIF = "ACTIF"
private const val ENGAGEMENT_CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"

/** CU8 (créer), CU9 (engager un club), CU13 (modifier les paramètres). */
@Service
class DeuxZeroService(
    private val deuxZeroRepository: DeuxZeroRepository,
    private val campRepository: CampRepository,
    private val saisonRepository: SaisonRepository,
    private val clubRepository: ClubRepository,
    private val adhesionClubRepository: AdhesionClubRepository,
    private val userRepository: UserRepository,
    private val effectifEngageRepository: EffectifEngageRepository,
) {

    @Transactional
    fun createDeuxZero(userId: UUID, request: CreateDeuxZeroRequest): DeuxZeroDto {
        validateCreateDeuxZeroRequest(request).orThrow()
        val organisateur = findUser(userId)

        val deuxZero = deuxZeroRepository.save(
            DeuxZeroEntity(
                nom = request.nom.trim(),
                lieu = request.lieu,
                joursHabituels = request.joursHabituels,
                heureHabituelle = request.heureHabituelle?.let { LocalTime.parse(it) },
                dureeMiTempsMinutes = request.dureeMiTempsMinutes,
                nombreMiTemps = request.nombreMiTemps,
                dureePauseMinutes = request.dureePauseMinutes,
                nombreMaxJoueursParCamp = request.nombreMaxJoueursParCamp,
                delaiValidationAutomatiqueHeures = request.delaiValidationAutomatiqueHeures,
                transfertsAutorisesParSaison = request.transfertsAutorisesParSaison,
                visibilite = request.visibilite,
                estInterne = request.estInterne,
                statut = STATUT_EN_ATTENTE_ENGAGEMENT,
                organisateur = organisateur,
            )
        )

        campRepository.save(CampEntity(deuxZero = deuxZero, libelle = request.libelleCampA, codeEngagement = generateEngagementCode()))
        campRepository.save(CampEntity(deuxZero = deuxZero, libelle = request.libelleCampB, codeEngagement = generateEngagementCode()))

        saisonRepository.save(
            SaisonEntity(
                deuxZero = deuxZero,
                libelle = request.libelleSaison.trim(),
                dateDebut = LocalDate.parse(request.dateDebutSaison),
                statut = "EN_COURS",
            )
        )
        return deuxZero.toDto()
    }

    /** CU9 — l'administrateur du club saisit le code d'engagement d'un camp. */
    @Transactional
    fun engageClub(userId: UUID, request: EngageClubRequest): CampDto {
        validateEngageClubRequest(request).orThrow()
        val camp = campRepository.findByCodeEngagement(request.codeEngagement.trim())
            ?: throw ResourceNotFoundException("Ce code ne correspond à aucun camp")
        if (camp.club != null) throw ConflictException("Ce camp est déjà engagé")

        // L'appelant doit administrer AU MOINS un club ; on prend le premier trouvé par cohérence
        // avec l'intention "j'engage mon club" — l'UI ne propose que les clubs que l'utilisateur administre.
        val adminMemberships = adhesionClubRepository.findAllByUtilisateur_Id(userId)
            .filter { it.role == ROLE_ADMIN && it.statut == STATUT_ACCEPTEE }
        if (adminMemberships.isEmpty()) {
            throw ForbiddenActionException("Il faut être administrateur d'un club pour l'engager")
        }
        val club = adminMemberships.first().club

        val updated = campRepository.save(
            CampEntity(
                id = camp.id,
                deuxZero = camp.deuxZero,
                club = club,
                libelle = camp.libelle,
                codeEngagement = camp.codeEngagement,
                capitaine = camp.capitaine,
                viceCapitaine = camp.viceCapitaine,
            )
        )
        activateIfBothCampsReady(camp.deuxZero.id!!)
        return updated.toDto()
    }

    /** CU9 étape 4 : capitaine/vice-capitaine, uniquement parmi les membres déjà engagés (CU10). */
    @Transactional
    fun nommerCapitaine(campId: UUID, requesterId: UUID, request: NommerCapitaineRequest): CampDto {
        val camp = campRepository.findById(campId).orElseThrow { ResourceNotFoundException("Camp introuvable") }
        requireClubAdmin(camp.club?.id, requesterId)
        val target = findUser(UUID.fromString(request.utilisateurId))

        val updated = campRepository.save(
            CampEntity(
                id = camp.id,
                deuxZero = camp.deuxZero,
                club = camp.club,
                libelle = camp.libelle,
                codeEngagement = camp.codeEngagement,
                capitaine = if (request.viceCapitaine) camp.capitaine else target,
                viceCapitaine = if (request.viceCapitaine) target else camp.viceCapitaine,
            )
        )
        activateIfBothCampsReady(camp.deuxZero.id!!)
        return updated.toDto()
    }

    @Transactional
    fun listCamps(deuxZeroId: UUID): List<CampDto> =
        campRepository.findAllByDeuxZero_Id(deuxZeroId).map { it.toDto() }

    /** Pas un CU numéroté — nécessaire pour atteindre un 2-0 sans le faire transiter par la
     * navigation mobile (E05/E06). Un 2-0 est "mien" si j'en suis l'organisateur, le capitaine ou
     * le vice-capitaine d'un camp, ou un membre actif de l'effectif engagé d'un camp.
     * @Transactional : [CampEntity.deuxZero] et [EffectifEngageEntity.camp] sont des relations LAZY
     * navigées directement ci-dessous (plus de lookup manuel par repository). */
    @Transactional
    fun listMesDeuxZero(userId: UUID): List<DeuxZeroDto> {
        val organises = deuxZeroRepository.findAllByOrganisateur_Id(userId).mapNotNull { it.id }
        val capitaine = campRepository.findAllByCapitaine_IdOrViceCapitaine_Id(userId, userId).mapNotNull { it.deuxZero.id }
        val joueur = effectifEngageRepository.findAllByUtilisateur_IdAndDateSortieIsNull(userId)
            .mapNotNull { it.camp.deuxZero.id }

        val deuxZeroIds = (organises + capitaine + joueur).distinct()
        return deuxZeroRepository.findAllById(deuxZeroIds).map { it.toDto() }
    }

    /** CU13 — ne s'applique qu'aux matchs à venir (contrôlé au moment de la saisie des matchs, Jalon 2/3). */
    @Transactional
    fun updateSettings(deuxZeroId: UUID, requesterId: UUID, request: UpdateDeuxZeroSettingsRequest): DeuxZeroDto {
        val deuxZero = requireOrganisateur(deuxZeroId, requesterId)
        return deuxZeroRepository.save(
            DeuxZeroEntity(
                id = deuxZero.id,
                nom = deuxZero.nom,
                lieu = deuxZero.lieu,
                joursHabituels = request.joursHabituels ?: deuxZero.joursHabituels,
                heureHabituelle = request.heureHabituelle?.let { LocalTime.parse(it) } ?: deuxZero.heureHabituelle,
                dureeMiTempsMinutes = request.dureeMiTempsMinutes ?: deuxZero.dureeMiTempsMinutes,
                nombreMiTemps = deuxZero.nombreMiTemps,
                dureePauseMinutes = deuxZero.dureePauseMinutes,
                nombreMaxJoueursParCamp = deuxZero.nombreMaxJoueursParCamp,
                delaiValidationAutomatiqueHeures = request.delaiValidationAutomatiqueHeures ?: deuxZero.delaiValidationAutomatiqueHeures,
                transfertsAutorisesParSaison = request.transfertsAutorisesParSaison ?: deuxZero.transfertsAutorisesParSaison,
                visibilite = request.visibilite ?: deuxZero.visibilite,
                estInterne = deuxZero.estInterne,
                statut = request.statut ?: deuxZero.statut,
                organisateur = deuxZero.organisateur,
            )
        ).toDto()
    }

    /** F-DZ-09 — transfert du rôle d'organisateur. */
    @Transactional
    fun transferOrganisateur(deuxZeroId: UUID, requesterId: UUID, targetUserId: UUID): DeuxZeroDto {
        val deuxZero = requireOrganisateur(deuxZeroId, requesterId)
        val target = findUser(targetUserId)
        return deuxZeroRepository.save(
            DeuxZeroEntity(
                id = deuxZero.id,
                nom = deuxZero.nom,
                lieu = deuxZero.lieu,
                joursHabituels = deuxZero.joursHabituels,
                heureHabituelle = deuxZero.heureHabituelle,
                dureeMiTempsMinutes = deuxZero.dureeMiTempsMinutes,
                nombreMiTemps = deuxZero.nombreMiTemps,
                dureePauseMinutes = deuxZero.dureePauseMinutes,
                nombreMaxJoueursParCamp = deuxZero.nombreMaxJoueursParCamp,
                delaiValidationAutomatiqueHeures = deuxZero.delaiValidationAutomatiqueHeures,
                transfertsAutorisesParSaison = deuxZero.transfertsAutorisesParSaison,
                visibilite = deuxZero.visibilite,
                estInterne = deuxZero.estInterne,
                statut = deuxZero.statut,
                organisateur = target,
            )
        ).toDto()
    }

    private fun activateIfBothCampsReady(deuxZeroId: UUID) {
        val camps = campRepository.findAllByDeuxZero_Id(deuxZeroId)
        val bothEngagedWithCaptain = camps.size == 2 && camps.all { it.club != null && it.capitaine != null }
        if (bothEngagedWithCaptain) {
            val deuxZero = deuxZeroRepository.findById(deuxZeroId).orElseThrow { ResourceNotFoundException("2-0 introuvable") }
            if (deuxZero.statut != STATUT_ACTIF) {
                deuxZeroRepository.save(
                    DeuxZeroEntity(
                        id = deuxZero.id, nom = deuxZero.nom, lieu = deuxZero.lieu,
                        joursHabituels = deuxZero.joursHabituels, heureHabituelle = deuxZero.heureHabituelle,
                        dureeMiTempsMinutes = deuxZero.dureeMiTempsMinutes, nombreMiTemps = deuxZero.nombreMiTemps,
                        dureePauseMinutes = deuxZero.dureePauseMinutes, nombreMaxJoueursParCamp = deuxZero.nombreMaxJoueursParCamp,
                        delaiValidationAutomatiqueHeures = deuxZero.delaiValidationAutomatiqueHeures,
                        transfertsAutorisesParSaison = deuxZero.transfertsAutorisesParSaison,
                        visibilite = deuxZero.visibilite, estInterne = deuxZero.estInterne,
                        statut = STATUT_ACTIF, organisateur = deuxZero.organisateur,
                    )
                )
            }
        }
    }

    /** Accès à [DeuxZeroEntity.organisateur] (relation LAZY) : les deux appelants
     * (updateSettings, transferOrganisateur) sont déjà @Transactional. */
    private fun requireOrganisateur(deuxZeroId: UUID, userId: UUID): DeuxZeroEntity {
        val deuxZero = deuxZeroRepository.findById(deuxZeroId).orElseThrow { ResourceNotFoundException("2-0 introuvable") }
        if (deuxZero.organisateur.id != userId) {
            throw ForbiddenActionException("Seul l'organisateur du 2-0 peut faire cette action")
        }
        return deuxZero
    }

    private fun requireClubAdmin(clubId: UUID?, userId: UUID) {
        val adhesion = clubId?.let { adhesionClubRepository.findByUtilisateur_IdAndClub_Id(userId, it) }
        if (adhesion?.role != ROLE_ADMIN || adhesion.statut != STATUT_ACCEPTEE) {
            throw ForbiddenActionException("Seul un administrateur du club peut faire cette action")
        }
    }

    private fun findUser(userId: UUID) =
        userRepository.findById(userId).orElseThrow { ResourceNotFoundException("Utilisateur introuvable") }

    private fun generateEngagementCode(): String = (1..8).map { ENGAGEMENT_CODE_CHARS.random(Random) }.joinToString("")

    /** Accès à [DeuxZeroEntity.organisateur] (relation LAZY) : tous les appelants de [toDto] sont
     * déjà @Transactional (createDeuxZero, updateSettings, transferOrganisateur, activateIfBothCampsReady,
     * listMesDeuxZero). */
    private fun DeuxZeroEntity.toDto() = DeuxZeroDto(
        id = id.toString(), nom = nom, lieu = lieu, visibilite = visibilite, estInterne = estInterne,
        joursHabituels = joursHabituels, heureHabituelle = heureHabituelle?.toString(),
        dureeMiTempsMinutes = dureeMiTempsMinutes, nombreMiTemps = nombreMiTemps, dureePauseMinutes = dureePauseMinutes,
        nombreMaxJoueursParCamp = nombreMaxJoueursParCamp, delaiValidationAutomatiqueHeures = delaiValidationAutomatiqueHeures,
        transfertsAutorisesParSaison = transfertsAutorisesParSaison, statut = statut, organisateurId = organisateur.id.toString(),
    )

    /** Accès à [CampEntity.deuxZero]/[CampEntity.club]/[CampEntity.capitaine]/[CampEntity.viceCapitaine]
     * (relations LAZY) : tous les appelants (engageClub, nommerCapitaine, listCamps) sont @Transactional. */
    private fun CampEntity.toDto(): CampDto = CampDto(
        id = id.toString(), deuxZeroId = deuxZero.id.toString(), clubId = club?.id?.toString(), clubNom = club?.nom,
        libelle = libelle, codeEngagement = codeEngagement, capitaineId = capitaine?.id?.toString(),
        capitainePseudo = capitaine?.pseudo, viceCapitaineId = viceCapitaine?.id?.toString(), viceCapitainePseudo = viceCapitaine?.pseudo,
    )
}
