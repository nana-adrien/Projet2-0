package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.CreateDeuxZeroInput
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** CU8 — Créer un 2-0 (E24). */
class CreateDeuxZeroUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(input: CreateDeuxZeroInput): Result<DeuxZero?, DeuxZeroError> =
        repository.createDeuxZero(input).mapFailure { it.toDeuxZeroError() }
}
