package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** CU10 — Effectif actuellement engagé dans un camp. */
class ListEffectifUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(campId: String): Result<List<Effectif>?, DeuxZeroError> =
        repository.listEffectif(campId).mapFailure { it.toDeuxZeroError() }
}
