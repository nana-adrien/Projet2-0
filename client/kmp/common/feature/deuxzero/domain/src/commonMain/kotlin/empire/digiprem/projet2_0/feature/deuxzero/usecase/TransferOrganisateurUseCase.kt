package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** F-DZ-09 — Transférer le rôle d'organisateur du 2-0. */
class TransferOrganisateurUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(deuxZeroId: String, targetUserId: String): Result<DeuxZero?, DeuxZeroError> =
        repository.transferOrganisateur(deuxZeroId, targetUserId).mapFailure { it.toDeuxZeroError() }
}
