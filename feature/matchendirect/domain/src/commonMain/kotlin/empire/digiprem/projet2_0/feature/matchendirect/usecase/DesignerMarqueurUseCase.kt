package empire.digiprem.projet2_0.feature.matchendirect.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.matchendirect.error.MatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.error.toMatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.repository.IMatchEnDirectRepository

/** F-LIV-01 — un marqueur et un suppléant, désignés par un capitaine du camp concerné. */
class DesignerMarqueurUseCase(private val repository: IMatchEnDirectRepository) {
    suspend operator fun invoke(matchId: String, utilisateurId: String, suppleant: Boolean): Result<MatchEnDirect?, MatchEnDirectError> =
        repository.designerMarqueur(matchId, utilisateurId, suppleant).mapFailure { it.toMatchEnDirectError() }
}
