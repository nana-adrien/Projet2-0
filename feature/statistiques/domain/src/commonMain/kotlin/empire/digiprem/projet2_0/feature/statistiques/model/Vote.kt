package empire.digiprem.projet2_0.feature.statistiques.model

import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement

data class Vote(
    val id: String,
    val matchId: String,
    val votantId: String,
    val candidatId: String,
    val horodatage: String,
)

data class Candidat(val utilisateurId: String, val pseudo: String, val campId: String)

/** E20 — Vote homme du match (CU24, RG-16). */
data class VoteHommeDuMatch(
    val match: Match,
    val evenements: List<Evenement>,
    val eligibles: List<Candidat>,
    val votesParCandidat: Map<String, Int>,
    val monVoteCandidatId: String?,
    val heuresRestantes: Int,
) {
    val nombreVotes: Int get() = votesParCandidat.values.sum()
    val nombreEligibles: Int get() = eligibles.size
}
