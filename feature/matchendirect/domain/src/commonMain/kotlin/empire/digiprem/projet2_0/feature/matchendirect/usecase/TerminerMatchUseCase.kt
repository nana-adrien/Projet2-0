package empire.digiprem.projet2_0.feature.matchendirect.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.matchendirect.error.MatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.error.toMatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.repository.IMatchEnDirectRepository

/** CU17 étape 6 — fin du temps réglementaire (le match passe en attente de validation, CU19). */
class TerminerMatchUseCase(private val repository: IMatchEnDirectRepository) {
    suspend operator fun invoke(matchId: String, periodeTerminee: Int): Result<MatchEnDirect?, MatchEnDirectError> =
        repository.terminerMatch(matchId, periodeTerminee).mapFailure { it.toMatchEnDirectError() }
}
