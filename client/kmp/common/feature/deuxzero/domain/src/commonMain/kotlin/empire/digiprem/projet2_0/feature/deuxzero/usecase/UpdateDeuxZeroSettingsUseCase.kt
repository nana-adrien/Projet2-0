package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero
import empire.digiprem.projet2_0.feature.deuxzero.model.UpdateDeuxZeroSettingsInput
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** CU13 — Modifier les paramètres d'un 2-0 (F-DZ-08). Ne s'applique qu'aux matchs à venir. */
class UpdateDeuxZeroSettingsUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(deuxZeroId: String, input: UpdateDeuxZeroSettingsInput): Result<DeuxZero?, DeuxZeroError> =
        repository.updateSettings(deuxZeroId, input).mapFailure { it.toDeuxZeroError() }
}
