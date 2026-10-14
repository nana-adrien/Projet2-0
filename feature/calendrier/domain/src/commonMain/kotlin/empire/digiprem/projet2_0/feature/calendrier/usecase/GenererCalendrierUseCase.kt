package empire.digiprem.projet2_0.feature.calendrier.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.calendrier.error.CalendrierError
import empire.digiprem.projet2_0.feature.calendrier.error.toCalendrierError
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.repository.ICalendrierRepository

/** CU14 (F-CAL-01) — Génération récurrente du calendrier (E07). */
class GenererCalendrierUseCase(private val repository: ICalendrierRepository) {
    suspend operator fun invoke(deuxZeroId: String): Result<List<Match>?, CalendrierError> =
        repository.genererCalendrier(deuxZeroId).mapFailure { it.toCalendrierError() }
}
