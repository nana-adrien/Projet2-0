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
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.SaisonRepository
import empire.digiprem.projet2_0.server.notifications.infra.entity.NotificationEntity
import empire.digiprem.projet2_0.server.notifications.infra.repository.NotificationRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
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
    private val saisonRepository: SaisonRepository,
    private val campRepository: CampRepository,
    private val effectifEngageRepository: EffectifEngageRepository,
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
    private val droits: CalendrierDroitsService,
) {

    @Transactional
    fun genererConvocations(matchId: UUID, requesterId: UUID): List<ConvocationDto> {
        val match = findMatchDeuxZero(matchId)
        val saisonId = match.saisonId ?: throw ResourceNotFoundException("Saison introuvable pour ce match")
        val deuxZeroId = droits.requireGestionnaireCalendrierPourSaison(saisonId, requesterId)

        val membresActifs = membresActifsDesDeuxCamps(deuxZeroId)
        val dejaConvoques = convocationRepository.findAllByMatchId(matchId).map { it.utilisateurId }.toSet()

        val nouvelles = membresActifs.filterNot { it in dejaConvoques }.map { utilisateurId ->
            ConvocationEntity(matchId = matchId, utilisateurId = utilisateurId, reponse = REPONSE_SANS_REPONSE)
        }
        val creees = convocationRepository.saveAll(nouvelles)

        creees.forEach { convocation ->
            notificationRepository.save(
                NotificationEntity(
                    userId = convocation.utilisateurId,
                    title = TITRE_CONVOCATION,
                    body = "Tu es convoqué pour le match du ${match.date} à ${match.heure}",
                )
            )
        }

        return convocationRepository.findAllByMatchId(matchId).map { it.toDto() }
    }

    fun listConvocations(matchId: UUID): List<ConvocationDto> =
        convocationRepository.findAllByMatchId(matchId).map { it.toDto() }

    @Transactional
    fun repondreConvocation(convocationId: UUID, requesterId: UUID, request: RepondreConvocationRequest): ConvocationDto {
        validateRepondreConvocationRequest(request).orThrow()
        val convocation = convocationRepository.findById(convocationId)
            .orElseThrow { ResourceNotFoundException("Convocation introuvable") }
        if (convocation.utilisateurId != requesterId) {
            throw ForbiddenActionException("Seul le joueur convoqué peut répondre à sa convocation")
        }

        val updated = convocationRepository.save(
            ConvocationEntity(
                id = convocation.id,
                matchId = convocation.matchId,
                utilisateurId = convocation.utilisateurId,
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
        val saisonId = match.saisonId ?: throw ResourceNotFoundException("Saison introuvable pour ce match")
        droits.requireGestionnaireCalendrierPourSaison(saisonId, requesterId)

        val sansReponse = convocationRepository.findAllByMatchId(matchId).filter { it.reponse == REPONSE_SANS_REPONSE }
        sansReponse.forEach { convocation ->
            notificationRepository.save(
                NotificationEntity(
                    userId = convocation.utilisateurId,
                    title = TITRE_RAPPEL,
                    body = "Rappel : réponds à ta convocation pour le match du ${match.date} à ${match.heure}",
                )
            )
        }
        return sansReponse.map { it.toDto() }
    }

    fun getPresences(matchId: UUID, requesterId: UUID): PresencesDto {
        val match = findMatchDeuxZero(matchId)
        val saisonId = match.saisonId ?: throw ResourceNotFoundException("Saison introuvable pour ce match")
        val deuxZeroId = droits.requireGestionnaireCalendrierPourSaison(saisonId, requesterId)

        val convocations = convocationRepository.findAllByMatchId(matchId)
        val camps = campRepository.findAllByDeuxZeroId(deuxZeroId)

        val campsDto = camps.map { camp ->
            val membresDuCamp = camp.id?.let { effectifEngageRepository.findAllByCampId(it) }
                ?.filter { it.dateSortie == null }
                ?.map { it.utilisateurId }
                ?.toSet() ?: emptySet()
            val reponsesDuCamp = convocations.filter { it.utilisateurId in membresDuCamp }

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

    private fun membresActifsDesDeuxCamps(deuxZeroId: UUID): List<UUID> =
        campRepository.findAllByDeuxZeroId(deuxZeroId)
            .mapNotNull { it.id }
            .flatMap { effectifEngageRepository.findAllByCampId(it) }
            .filter { it.dateSortie == null }
            .map { it.utilisateurId }
            .distinct()

    private fun findMatchDeuxZero(matchId: UUID): MatchEntity {
        val match = matchRepository.findById(matchId).orElseThrow { ResourceNotFoundException("Match introuvable") }
        if (match.type != TYPE_DEUX_ZERO) {
            throw ForbiddenActionException("Seuls les matchs de 2-0 suivent le mécanisme de convocation (CU15)")
        }
        return match
    }

    private fun ConvocationEntity.toDto(): ConvocationDto {
        val pseudo = userRepository.findById(utilisateurId).map { it.pseudo }.orElse("")
        return ConvocationDto(
            id = id.toString(),
            matchId = matchId.toString(),
            utilisateurId = utilisateurId.toString(),
            pseudo = pseudo,
            reponse = reponse,
            dateReponse = dateReponse?.toString(),
        )
    }
}
