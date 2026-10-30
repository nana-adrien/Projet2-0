package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** CU9 étape 4 / CU10 (F-EFF-03) — Nommer ou changer le capitaine/vice-capitaine d'un camp. */
class NommerCapitaineUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(campId: String, utilisateurId: String, viceCapitaine: Boolean): Result<Camp?, DeuxZeroError> =
        repository.nommerCapitaine(campId, utilisateurId, viceCapitaine).mapFailure { it.toDeuxZeroError() }
}
