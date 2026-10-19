package empire.digiprem.projet2_0.server.statistiques.service

import empire.digiprem.projet2_0.contracts.statistiques.ClassementEntryDto
import empire.digiprem.projet2_0.contracts.statistiques.VoteDto
import empire.digiprem.projet2_0.contracts.statistiques.VoteRequest
import empire.digiprem.projet2_0.contracts.statistiques.VoteResultatDto
import empire.digiprem.projet2_0.contracts.validation.validateVoteRequest
import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.calendrier.infra.repository.CompositionRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.EvenementRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.MatchRepository
import empire.digiprem.projet2_0.server.common.exception.ConflictException
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.common.exception.ValidationException
import empire.digiprem.projet2_0.server.common.util.orThrow
import empire.digiprem.projet2_0.server.statistiques.infra.entity.VoteEntity
import empire.digiprem.projet2_0.server.statistiques.infra.repository.VoteRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

private const val STATUT_OFFICIEL_MATCH = "OFFICIEL"
private const val FENETRE_VOTE_HEURES = 24L

/**
 * CU24 (vote "homme du match"). RG-16 : un seul vote par joueur et par match, jamais pour
 * soi-même, le votant doit être présent sur la feuille de match, fenêtre de vote de 24h.
 *
 * **Fenêtre de vote — choix temporaire.** Il n'existe pas encore de timestamp explicite
 * "officialisation du match" (CU19/CU20, Jalon 4, en cours de récupération en parallèle, non
 * disponible ici). On utilise comme proxy le dernier événement non annulé du match
 * ([EvenementRepository.findFirstByMatchIdAndAnnuleFalseOrderByHorodatageDesc], typiquement un
 * FIN_PERIODE) + 24h. **À revoir dès que CU19/20 ajoutera un vrai timestamp d'officialisation.**
 *
 * **Présence — interprétation.** [CompositionEntity.present] est positionné par le capitaine
 * avant le match via `designerComposition` (CompositionService) ; en l'absence d'un modèle de
 * présence post-match distinct, c'est le meilleur proxy disponible pour "présent sur la feuille
 * de match".
 */
@Service
class VoteService(
    private val voteRepository: VoteRepository,
    private val matchRepository: MatchRepository,
    private val compositionRepository: CompositionRepository,
    private val evenementRepository: EvenementRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun voter(matchId: UUID, votantId: UUID, request: VoteRequest): VoteDto {
        validateVoteRequest(request).orThrow()
        val match = findMatch(matchId)
        if (match.statut != STATUT_OFFICIEL_MATCH) {
            throw ForbiddenActionException("Le vote n'est ouvert que pour un match OFFICIEL")
        }

        val candidatId = UUID.fromString(request.candidatId)
        if (candidatId == votantId) {
            throw ValidationException(listOf("candidatId: impossible de voter pour soi-même"))
        }

        val compositionVotant = compositionRepository.findByMatchIdAndUtilisateurId(matchId, votantId)
        if (compositionVotant == null || !compositionVotant.present) {
            throw ForbiddenActionException("Seul un joueur présent sur la feuille de match peut voter")
        }

        if (!fenetreVoteOuverte(matchId)) {
            throw ForbiddenActionException("La fenêtre de vote (24h après la fin du match) est fermée")
        }

        if (voteRepository.findByMatchIdAndVotantId(matchId, votantId) != null) {
            throw ConflictException("Ce joueur a déjà voté pour ce match")
        }

        val saved = voteRepository.save(VoteEntity(matchId = matchId, votantId = votantId, candidatId = candidatId))
        return saved.toDto()
    }

    fun resultat(matchId: UUID): VoteResultatDto {
        findMatch(matchId)
        val votes = voteRepository.findAllByMatchId(matchId)
        val voixParCandidat = votes.groupingBy { it.candidatId }.eachCount()
        val gagnants = calculerGagnantsVote(votes)

        return VoteResultatDto(
            gagnants = gagnants.map { it.toString() },
            voix = voixParCandidat
                .entries
                .sortedByDescending { it.value }
                .map { (userId, nb) -> ClassementEntryDto(userId.toString(), pseudoDe(userId), nb) },
            fenetreOuverte = fenetreVoteOuverte(matchId),
        )
    }

    /** Fenêtre de 24h depuis le dernier événement non annulé du match (cf. KDoc de la classe). */
    private fun fenetreVoteOuverte(matchId: UUID): Boolean {
        val dernierEvenement = evenementRepository.findFirstByMatchIdAndAnnuleFalseOrderByHorodatageDesc(matchId)
            ?: return false
        return Instant.now().isBefore(dernierEvenement.horodatage.plus(FENETRE_VOTE_HEURES, ChronoUnit.HOURS))
    }

    private fun findMatch(matchId: UUID): MatchEntity =
        matchRepository.findById(matchId).orElseThrow { ResourceNotFoundException("Match introuvable") }

    private fun pseudoDe(userId: UUID): String = userRepository.findById(userId).map { it.pseudo }.orElse("")

    private fun VoteEntity.toDto() = VoteDto(
        id = id.toString(),
        matchId = matchId.toString(),
        votantId = votantId.toString(),
        candidatId = candidatId.toString(),
        horodatage = horodatage.toString(),
    )
}
