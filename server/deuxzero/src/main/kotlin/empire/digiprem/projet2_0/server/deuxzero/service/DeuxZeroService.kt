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
) {

    @Transactional
    fun createDeuxZero(userId: UUID, request: CreateDeuxZeroRequest): DeuxZeroDto {
        validateCreateDeuxZeroRequest(request).orThrow()

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
                organisateurId = userId,
            )
        )
        val deuxZeroId = deuxZero.id ?: throw ResourceNotFoundException("2-0 introuvable après création")

        campRepository.save(CampEntity(deuxZeroId = deuxZeroId, libelle = request.libelleCampA, codeEngagement = generateEngagementCode()))
        campRepository.save(CampEntity(deuxZeroId = deuxZeroId, libelle = request.libelleCampB, codeEngagement = generateEngagementCode()))

        saisonRepository.save(
            SaisonEntity(
                deuxZeroId = deuxZeroId,
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
        if (camp.clubId != null) throw ConflictException("Ce camp est déjà engagé")

        // L'appelant doit administrer AU MOINS un club ; on prend le premier trouvé par cohérence
        // avec l'intention "j'engage mon club" — l'UI ne propose que les clubs que l'utilisateur administre.
        val adminMemberships = adhesionClubRepository.findAllByUtilisateurId(userId)
            .filter { it.role == ROLE_ADMIN && it.statut == STATUT_ACCEPTEE }
        if (adminMemberships.isEmpty()) {
            throw ForbiddenActionException("Il faut être administrateur d'un club pour l'engager")
        }
        val club = clubRepository.findById(adminMemberships.first().clubId).orElseThrow {
            ResourceNotFoundException("Club introuvable")
        }

        val updated = campRepository.save(
            CampEntity(
                id = camp.id,
                deuxZeroId = camp.deuxZeroId,
                clubId = club.id,
                libelle = camp.libelle,
                codeEngagement = camp.codeEngagement,
                capitaineId = camp.capitaineId,
                viceCapitaineId = camp.viceCapitaineId,
            )
        )
        activateIfBothCampsReady(camp.deuxZeroId)
        return updated.toDto(club.nom)
    }

    /** CU9 étape 4 : capitaine/vice-capitaine, uniquement parmi les membres déjà engagés (CU10). */
    @Transactional
    fun nommerCapitaine(campId: UUID, requesterId: UUID, request: NommerCapitaineRequest): CampDto {
        val camp = campRepository.findById(campId).orElseThrow { ResourceNotFoundException("Camp introuvable") }
        requireClubAdmin(camp.clubId, requesterId)
        val targetId = UUID.fromString(request.utilisateurId)

        val updated = campRepository.save(
            CampEntity(
                id = camp.id,
                deuxZeroId = camp.deuxZeroId,
                clubId = camp.clubId,
                libelle = camp.libelle,
                codeEngagement = camp.codeEngagement,
                capitaineId = if (request.viceCapitaine) camp.capitaineId else targetId,
                viceCapitaineId = if (request.viceCapitaine) targetId else camp.viceCapitaineId,
            )
        )
        activateIfBothCampsReady(camp.deuxZeroId)
        return updated.toDto(clubRepository.findById(camp.clubId!!).orElseThrow { ResourceNotFoundException("Club introuvable") }.nom)
    }

    fun listCamps(deuxZeroId: UUID): List<CampDto> =
        campRepository.findAllByDeuxZeroId(deuxZeroId).map { camp ->
            val clubNom = camp.clubId?.let { clubRepository.findById(it).map { c -> c.nom }.orElse(null) }
            camp.toDto(clubNom)
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
                organisateurId = deuxZero.organisateurId,
            )
        ).toDto()
    }

    /** F-DZ-09 — transfert du rôle d'organisateur. */
    @Transactional
    fun transferOrganisateur(deuxZeroId: UUID, requesterId: UUID, targetUserId: UUID): DeuxZeroDto {
        val deuxZero = requireOrganisateur(deuxZeroId, requesterId)
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
                organisateurId = targetUserId,
            )
        ).toDto()
    }

    private fun activateIfBothCampsReady(deuxZeroId: UUID) {
        val camps = campRepository.findAllByDeuxZeroId(deuxZeroId)
        val bothEngagedWithCaptain = camps.size == 2 && camps.all { it.clubId != null && it.capitaineId != null }
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
                        statut = STATUT_ACTIF, organisateurId = deuxZero.organisateurId,
                    )
                )
            }
        }
    }

    private fun requireOrganisateur(deuxZeroId: UUID, userId: UUID): DeuxZeroEntity {
        val deuxZero = deuxZeroRepository.findById(deuxZeroId).orElseThrow { ResourceNotFoundException("2-0 introuvable") }
        if (deuxZero.organisateurId != userId) {
            throw ForbiddenActionException("Seul l'organisateur du 2-0 peut faire cette action")
        }
        return deuxZero
    }

    private fun requireClubAdmin(clubId: UUID?, userId: UUID) {
        val adhesion = clubId?.let { adhesionClubRepository.findByUtilisateurIdAndClubId(userId, it) }
        if (adhesion?.role != ROLE_ADMIN || adhesion.statut != STATUT_ACCEPTEE) {
            throw ForbiddenActionException("Seul un administrateur du club peut faire cette action")
        }
    }

    private fun generateEngagementCode(): String = (1..8).map { ENGAGEMENT_CODE_CHARS.random(Random) }.joinToString("")

    private fun DeuxZeroEntity.toDto() = DeuxZeroDto(
        id = id.toString(), nom = nom, lieu = lieu, visibilite = visibilite, estInterne = estInterne,
        joursHabituels = joursHabituels, heureHabituelle = heureHabituelle?.toString(),
        dureeMiTempsMinutes = dureeMiTempsMinutes, nombreMiTemps = nombreMiTemps, dureePauseMinutes = dureePauseMinutes,
        nombreMaxJoueursParCamp = nombreMaxJoueursParCamp, delaiValidationAutomatiqueHeures = delaiValidationAutomatiqueHeures,
        transfertsAutorisesParSaison = transfertsAutorisesParSaison, statut = statut, organisateurId = organisateurId.toString(),
    )

    private fun CampEntity.toDto(clubNom: String?): CampDto {
        val capitainePseudo = capitaineId?.let { userRepository.findById(it).map { u -> u.pseudo }.orElse(null) }
        val viceCapitainePseudo = viceCapitaineId?.let { userRepository.findById(it).map { u -> u.pseudo }.orElse(null) }
        return CampDto(
            id = id.toString(), deuxZeroId = deuxZeroId.toString(), clubId = clubId?.toString(), clubNom = clubNom,
            libelle = libelle, codeEngagement = codeEngagement, capitaineId = capitaineId?.toString(),
            capitainePseudo = capitainePseudo, viceCapitaineId = viceCapitaineId?.toString(), viceCapitainePseudo = viceCapitainePseudo,
        )
    }
}
