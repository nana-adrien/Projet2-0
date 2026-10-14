package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** CU8/CU9 — Liste des deux camps d'un 2-0 (codes d'engagement, capitaines). */
class ListCampsUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(deuxZeroId: String): Result<List<Camp>?, DeuxZeroError> =
        repository.listCamps(deuxZeroId).mapFailure { it.toDeuxZeroError() }
}
