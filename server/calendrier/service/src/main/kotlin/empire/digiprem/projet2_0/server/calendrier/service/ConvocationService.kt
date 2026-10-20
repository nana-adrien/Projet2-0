package empire.digiprem.projet2_0.server.calendrier.service

import empire.digiprem.projet2_0.contracts.calendrier.ConvocationDto
import empire.digiprem.projet2_0.contracts.calendrier.PresencesCampDto
import empire.digiprem.projet2_0.contracts.calendrier.PresencesDto
import empire.digiprem.projet2_0.contracts.calendrier.RepondreConvocationRequest
import empire.digiprem.projet2_0.contracts.validation.validateRepondreConvocationRequest
import empire.digiprem.projet2_0.server.calendrier.infra.entity.ConvocationEntity
import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.calendrier.infra.repository.ConvocationRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.MatchRepository
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.common.util.orThrow
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.CampRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.EffectifEngageRepository
import empire.digiprem.projet2_0.server.notifications.infra.entity.NotificationEntity
import empire.digiprem.projet2_0.server.notifications.infra.repository.NotificationRepository
import empire.digiprem.projet2_0.server.user.infra.entity.UserEntity
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

private const val TYPE_DEUX_ZERO = "DEUX_ZERO"
private const val REPONSE_SANS_REPONSE = "SANS_REPONSE"
private const val REPONSE_PRESENT = "PRESENT"
private const val REPONSE_ABSENT = "ABSENT"
private const val REPONSE_INCERTAIN = "INCERTAIN"
private const val TITRE_CONVOCATION = "Convocation"
private const val TITRE_RAPPEL = "Rappel"

/**
 * CU15 — Convoquer les joueurs d'un match de 2-0, recueillir leurs réponses et compter les
 * présences (F-CAL-06). Un match AMICAL (CU21, Jalon 6) ne suit pas ce mécanisme.
 */
@Service
class ConvocationService(
    private val convocationRepository: ConvocationRepository,
    private val matchRepository: MatchRepository,
    private val campRepository: CampRepository,
    private val effectifEngageRepository: EffectifEngageRepository,
    private val notificationRepository: NotificationRepository,
    private val droits: CalendrierDroitsService,
) {

    @Transactional
    fun genererConvocations(matchId: UUID, requesterId: UUID): List<ConvocationDto> {
        val match = findMatchDeuxZero(matchId)
        val saisonId = match.saison?.id ?: throw ResourceNotFoundException("Saison introuvable pour ce match")
        val deuxZeroId = droits.requireGestionnaireCalendrierPourSaison(saisonId, requesterId)

        val membresActifs = membresActifsDesDeuxCamps(deuxZeroId)
        val dejaConvoques = convocationRepository.findAllByMatch_Id(matchId).map { it.utilisateur.id }.toSet()

        val nouvelles = membresActifs.filterNot { it.id in dejaConvoques }.map { utilisateur ->
            ConvocationEntity(match = match, utilisateur = utilisateur, reponse = REPONSE_SANS_REPONSE)
        }
        val creees = convocationRepository.saveAll(nouvelles)

        creees.forEach { convocation ->
            notificationRepository.save(
                NotificationEntity(
                    user = convocation.utilisateur,
                    title = TITRE_CONVOCATION,
                    body = "Tu es convoqué pour le match du ${match.date} à ${match.heure}",
                )
            )
        }

        return convocationRepository.findAllByMatch_Id(matchId).map { it.toDto() }
    }

    /** @Transactional : [ConvocationEntity.utilisateur] (relation LAZY) navigué par [toDto]. */
    @Transactional
    fun listConvocations(matchId: UUID): List<ConvocationDto> =
        convocationRepository.findAllByMatch_Id(matchId).map { it.toDto() }

    @Transactional
    fun repondreConvocation(convocationId: UUID, requesterId: UUID, request: RepondreConvocationRequest): ConvocationDto {
        validateRepondreConvocationRequest(request).orThrow()
        val convocation = convocationRepository.findById(convocationId)
            .orElseThrow { ResourceNotFoundException("Convocation introuvable") }
        if (convocation.utilisateur.id != requesterId) {
            throw ForbiddenActionException("Seul le joueur convoqué peut répondre à sa convocation")
        }

        val updated = convocationRepository.save(
            ConvocationEntity(
                id = convocation.id,
                match = convocation.match,
                utilisateur = convocation.utilisateur,
                reponse = request.reponse,
                dateReponse = Instant.now(),
            )
        )
        return updated.toDto()
    }

    /** Déclenchement manuel (bouton "relancer") — pas une tâche planifiée automatique
     * (aucun ordonnanceur en place au Jalon 2, limitation documentée). */
    @Transactional
    fun relancerConvocations(matchId: UUID, requesterId: UUID): List<ConvocationDto> {
        val match = findMatchDeuxZero(matchId)
        val saisonId = match.saison?.id ?: throw ResourceNotFoundException("Saison introuvable pour ce match")
        droits.requireGestionnaireCalendrierPourSaison(saisonId, requesterId)

        val sansReponse = convocationRepository.findAllByMatch_Id(matchId).filter { it.reponse == REPONSE_SANS_REPONSE }
        sansReponse.forEach { convocation ->
            notificationRepository.save(
                NotificationEntity(
                    user = convocation.utilisateur,
                    title = TITRE_RAPPEL,
                    body = "Rappel : réponds à ta convocation pour le match du ${match.date} à ${match.heure}",
                )
            )
        }
        return sansReponse.map { it.toDto() }
    }

    /** @Transactional : [CampEntity.libelle] ne pose pas de souci (colonne propre), mais
     * [EffectifEngageEntity.utilisateur] et [ConvocationEntity.utilisateur] (relations LAZY)
     * sont désormais comparées directement ci-dessous. */
    @Transactional
    fun getPresences(matchId: UUID, requesterId: UUID): PresencesDto {
        val match = findMatchDeuxZero(matchId)
        val saisonId = match.saison?.id ?: throw ResourceNotFoundException("Saison introuvable pour ce match")
        val deuxZeroId = droits.requireGestionnaireCalendrierPourSaison(saisonId, requesterId)

        val convocations = convocationRepository.findAllByMatch_Id(matchId)
        val camps = campRepository.findAllByDeuxZero_Id(deuxZeroId)

        val campsDto = camps.map { camp ->
            val membresDuCamp = camp.id?.let { effectifEngageRepository.findAllByCamp_Id(it) }
                ?.filter { it.dateSortie == null }
                ?.mapNotNull { it.utilisateur.id }
                ?.toSet() ?: emptySet()
            val reponsesDuCamp = convocations.filter { it.utilisateur.id in membresDuCamp }

            PresencesCampDto(
                campId = camp.id.toString(),
                libelle = camp.libelle,
                presents = reponsesDuCamp.count { it.reponse == REPONSE_PRESENT },
                absents = reponsesDuCamp.count { it.reponse == REPONSE_ABSENT },
                incertains = reponsesDuCamp.count { it.reponse == REPONSE_INCERTAIN },
                sansReponse = reponsesDuCamp.count { it.reponse == REPONSE_SANS_REPONSE },
            )
        }
        return PresencesDto(matchId = matchId.toString(), camps = campsDto)
    }

    private fun membresActifsDesDeuxCamps(deuxZeroId: UUID): List<UserEntity> =
        campRepository.findAllByDeuxZero_Id(deuxZeroId)
            .mapNotNull { it.id }
            .flatMap { effectifEngageRepository.findAllByCamp_Id(it) }
            .filter { it.dateSortie == null }
            .map { it.utilisateur }
            .distinctBy { it.id }

    private fun findMatchDeuxZero(matchId: UUID): MatchEntity {
        val match = matchRepository.findById(matchId).orElseThrow { ResourceNotFoundException("Match introuvable") }
        if (match.type != TYPE_DEUX_ZERO) {
            throw ForbiddenActionException("Seuls les matchs de 2-0 suivent le mécanisme de convocation (CU15)")
        }
        return match
    }

    /** Accès à [ConvocationEntity.utilisateur]/[ConvocationEntity.match] (relations LAZY) : tous
     * les appelants (genererConvocations, listConvocations, repondreConvocation,
     * relancerConvocations) sont @Transactional. */
    private fun ConvocationEntity.toDto(): ConvocationDto = ConvocationDto(
        id = id.toString(),
        matchId = match.id.toString(),
        utilisateurId = utilisateur.id.toString(),
        pseudo = utilisateur.pseudo,
        reponse = reponse,
        dateReponse = dateReponse?.toString(),
    )
}
