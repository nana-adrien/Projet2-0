package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** CU11 — Transférer un joueur vers l'autre camp du même 2-0 (RG-09 : clôture l'ancien effectif). */
class TransferPlayerUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(utilisateurId: String, nouveauCampId: String): Result<Effectif?, DeuxZeroError> =
        repository.transferPlayer(utilisateurId, nouveauCampId).mapFailure { it.toDeuxZeroError() }
}
