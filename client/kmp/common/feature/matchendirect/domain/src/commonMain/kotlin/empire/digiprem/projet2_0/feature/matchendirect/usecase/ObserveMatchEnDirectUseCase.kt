package empire.digiprem.projet2_0.feature.matchendirect.usecase

import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.repository.IMatchEnDirectRepository
import kotlinx.coroutines.flow.Flow

/** CU18 — flux WebSocket de l'état du match (score, chronologie), mis à jour à chaque saisie. */
class ObserveMatchEnDirectUseCase(private val repository: IMatchEnDirectRepository) {
    operator fun invoke(matchId: String): Flow<MatchEnDirect> = repository.observeMatchEnDirect(matchId)
}
