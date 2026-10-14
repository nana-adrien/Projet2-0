package empire.digiprem.projet2_0.feature.validation.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.validation.error.ValidationError
import empire.digiprem.projet2_0.feature.validation.error.toValidationError
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch
import empire.digiprem.projet2_0.feature.validation.repository.IValidationRepository

/** CU19 A1 — contestation avec correction proposée (E15). */
class ContesterResultatUseCase(private val repository: IValidationRepository) {
    suspend operator fun invoke(matchId: String, correctionProposee: String, commentaire: String?): Result<ResultatMatch?, ValidationError> =
        repository.contester(matchId, correctionProposee, commentaire).mapFailure { it.toValidationError() }
}
