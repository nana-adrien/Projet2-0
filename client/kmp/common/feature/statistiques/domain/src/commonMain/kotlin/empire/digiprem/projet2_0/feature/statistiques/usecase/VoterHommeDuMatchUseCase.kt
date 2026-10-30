package empire.digiprem.projet2_0.feature.statistiques.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.statistiques.error.StatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.error.toStatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.model.VoteHommeDuMatch
import empire.digiprem.projet2_0.feature.statistiques.repository.IStatistiquesRepository

/** E20 — Voter (CU24, RG-16 : une seule fois, pas pour soi-même). */
class VoterHommeDuMatchUseCase(private val repository: IStatistiquesRepository) {
    suspend operator fun invoke(matchId: String, candidatId: String): Result<VoteHommeDuMatch?, StatistiquesError> =
        repository.voterHommeDuMatch(matchId, candidatId).mapFailure { it.toStatistiquesError() }
}
