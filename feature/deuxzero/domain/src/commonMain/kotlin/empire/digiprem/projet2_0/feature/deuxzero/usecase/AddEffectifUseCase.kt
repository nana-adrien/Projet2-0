package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** CU10 — Ajouter un membre du club à l'effectif engagé d'un camp. */
class AddEffectifUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(campId: String, utilisateurId: String, numeroMaillot: Int?): Result<Effectif?, DeuxZeroError> =
        repository.addEffectif(campId, utilisateurId, numeroMaillot).mapFailure { it.toDeuxZeroError() }
}
