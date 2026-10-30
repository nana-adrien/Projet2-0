package empire.digiprem.projet2_0.feature.validation.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.validation.error.ValidationError
import empire.digiprem.projet2_0.feature.validation.error.toValidationError
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch
import empire.digiprem.projet2_0.feature.validation.repository.IValidationRepository

/** CU19/CU20 — récapitulatif du résultat (E14) ou du litige (E16). */
class GetResultatUseCase(private val repository: IValidationRepository) {
    suspend operator fun invoke(matchId: String): Result<ResultatMatch?, ValidationError> =
        repository.getResultat(matchId).mapFailure { it.toValidationError() }
}
