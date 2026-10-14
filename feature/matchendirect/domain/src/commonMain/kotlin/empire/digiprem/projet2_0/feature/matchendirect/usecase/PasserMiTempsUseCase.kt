package empire.digiprem.projet2_0.feature.matchendirect.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.matchendirect.error.MatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.error.toMatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.repository.IMatchEnDirectRepository

/** F-LIV-02 — clôt la période en cours et lance la suivante. */
class PasserMiTempsUseCase(private val repository: IMatchEnDirectRepository) {
    suspend operator fun invoke(matchId: String, periodeTerminee: Int): Result<MatchEnDirect?, MatchEnDirectError> =
        repository.passerMiTemps(matchId, periodeTerminee).mapFailure { it.toMatchEnDirectError() }
}
