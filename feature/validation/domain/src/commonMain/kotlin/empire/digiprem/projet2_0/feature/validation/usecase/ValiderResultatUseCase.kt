package empire.digiprem.projet2_0.feature.validation.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.validation.error.ValidationError
import empire.digiprem.projet2_0.feature.validation.error.toValidationError
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch
import empire.digiprem.projet2_0.feature.validation.repository.IValidationRepository

/** CU19 étape 2 — signature du camp (capitaine ou vice-capitaine), RG-12. */
class ValiderResultatUseCase(private val repository: IValidationRepository) {
    suspend operator fun invoke(matchId: String): Result<ResultatMatch?, ValidationError> =
        repository.valider(matchId).mapFailure { it.toValidationError() }
}
