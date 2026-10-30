package empire.digiprem.projet2_0.feature.calendrier.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.calendrier.error.CalendrierError
import empire.digiprem.projet2_0.feature.calendrier.error.toCalendrierError
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.repository.ICalendrierRepository

/** CU14 — Liste des matchs de la saison en cours d'un 2-0 (E07). */
class ListMatchsUseCase(private val repository: ICalendrierRepository) {
    suspend operator fun invoke(deuxZeroId: String): Result<List<Match>?, CalendrierError> =
        repository.listMatchs(deuxZeroId).mapFailure { it.toCalendrierError() }
}
