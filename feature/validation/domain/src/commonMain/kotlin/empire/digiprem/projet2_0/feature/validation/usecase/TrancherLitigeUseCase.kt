package empire.digiprem.projet2_0.feature.validation.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.validation.error.ValidationError
import empire.digiprem.projet2_0.feature.validation.error.toValidationError
import empire.digiprem.projet2_0.feature.validation.model.DecisionLitige
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch
import empire.digiprem.projet2_0.feature.validation.repository.IValidationRepository

/** CU20 — décision définitive de l'organisateur (RG-13), journal d'audit non modifiable. */
class TrancherLitigeUseCase(private val repository: IValidationRepository) {
    suspend operator fun invoke(matchId: String, decision: DecisionLitige, motif: String): Result<ResultatMatch?, ValidationError> =
        repository.trancherLitige(matchId, decision, motif).mapFailure { it.toValidationError() }
}
