package empire.digiprem.projet2_0.feature.validation.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.validation.error.ValidationError
import empire.digiprem.projet2_0.feature.validation.error.toValidationError
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch
import empire.digiprem.projet2_0.feature.validation.repository.IValidationRepository

/** CU19 A1 — le camp adverse accepte la correction (match corrigé puis officialisé) ou la
 * refuse (match -> En litige, CU20). */
class RepondreContestationUseCase(private val repository: IValidationRepository) {
    suspend operator fun invoke(matchId: String, accepter: Boolean, commentaire: String?): Result<ResultatMatch?, ValidationError> =
        repository.repondreContestation(matchId, accepter, commentaire).mapFailure { it.toValidationError() }
}
