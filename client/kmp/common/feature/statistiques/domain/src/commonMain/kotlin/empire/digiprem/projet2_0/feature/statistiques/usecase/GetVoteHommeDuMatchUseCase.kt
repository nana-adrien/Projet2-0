package empire.digiprem.projet2_0.feature.statistiques.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.statistiques.error.StatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.error.toStatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.model.VoteHommeDuMatch
import empire.digiprem.projet2_0.feature.statistiques.repository.IStatistiquesRepository

/** E20 — Vote homme du match (CU24). */
class GetVoteHommeDuMatchUseCase(private val repository: IStatistiquesRepository) {
    suspend operator fun invoke(matchId: String): Result<VoteHommeDuMatch?, StatistiquesError> =
        repository.getVoteHommeDuMatch(matchId).mapFailure { it.toStatistiquesError() }
}
