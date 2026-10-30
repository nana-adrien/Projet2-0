package empire.digiprem.projet2_0.feature.matchendirect.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.matchendirect.error.MatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.error.toMatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.model.Composition
import empire.digiprem.projet2_0.feature.matchendirect.repository.IMatchEnDirectRepository

class ListCompositionUseCase(private val repository: IMatchEnDirectRepository) {
    suspend operator fun invoke(matchId: String): Result<List<Composition>?, MatchEnDirectError> =
        repository.listComposition(matchId).mapFailure { it.toMatchEnDirectError() }
}
