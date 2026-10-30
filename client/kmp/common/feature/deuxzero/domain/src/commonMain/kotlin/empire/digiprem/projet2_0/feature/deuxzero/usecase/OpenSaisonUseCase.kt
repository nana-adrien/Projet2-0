package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.Saison
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** CU12 — Ouvrir une nouvelle saison. */
class OpenSaisonUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(deuxZeroId: String, libelle: String, dateDebut: String, dateFin: String?): Result<Saison?, DeuxZeroError> =
        repository.openSaison(deuxZeroId, libelle, dateDebut, dateFin).mapFailure { it.toDeuxZeroError() }
}
