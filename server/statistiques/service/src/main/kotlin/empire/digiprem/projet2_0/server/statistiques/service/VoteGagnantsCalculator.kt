package empire.digiprem.projet2_0.server.statistiques.service

import empire.digiprem.projet2_0.server.statistiques.infra.entity.VoteEntity
import java.util.UUID

/** RG-16 — en cas d'égalité de voix, "la distinction est partagée" : renvoie TOUS les candidats
 * ayant obtenu le maximum de voix, jamais un seul gagnant arbitraire. Partagé entre [VoteService]
 * (résultat d'un match) et [StatistiquesService]/[BilanService] (classement "homme du match"). */
internal fun calculerGagnantsVote(votes: List<VoteEntity>): List<UUID> {
    if (votes.isEmpty()) return emptyList()
    val voixParCandidat = votes.groupingBy { it.candidatId }.eachCount()
    val maxVoix = voixParCandidat.values.max()
    return voixParCandidat.filterValues { it == maxVoix }.keys.toList()
}
