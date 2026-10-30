package empire.digiprem.projet2_0.feature.matchendirect.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.matchendirect.error.MatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.error.toMatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.repository.IMatchEnDirectRepository

/** CU17 étape 1 — coup d'envoi (RG-11 : marqueur désigné ou suppléant uniquement). */
class DemarrerMatchUseCase(private val repository: IMatchEnDirectRepository) {
    suspend operator fun invoke(matchId: String): Result<MatchEnDirect?, MatchEnDirectError> =
        repository.demarrerMatch(matchId).mapFailure { it.toMatchEnDirectError() }
}
