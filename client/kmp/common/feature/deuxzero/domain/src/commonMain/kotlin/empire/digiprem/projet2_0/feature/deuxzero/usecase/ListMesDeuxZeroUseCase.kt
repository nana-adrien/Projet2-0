package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** Les 2-0 où je suis organisateur, capitaine/vice-capitaine, ou membre actif d'un effectif (E05/E06). */
class ListMesDeuxZeroUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(): Result<List<DeuxZero>?, DeuxZeroError> =
        repository.listMesDeuxZero().mapFailure { it.toDeuxZeroError() }
}
