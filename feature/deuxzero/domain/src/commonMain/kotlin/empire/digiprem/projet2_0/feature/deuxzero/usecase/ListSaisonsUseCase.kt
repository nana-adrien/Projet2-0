package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.Saison
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** CU12 — Liste des saisons d'un 2-0. */
class ListSaisonsUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(deuxZeroId: String): Result<List<Saison>?, DeuxZeroError> =
        repository.listSaisons(deuxZeroId).mapFailure { it.toDeuxZeroError() }
}
