package empire.digiprem.projet2_0.feature.calendrier.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.calendrier.error.CalendrierError
import empire.digiprem.projet2_0.feature.calendrier.error.toCalendrierError
import empire.digiprem.projet2_0.feature.calendrier.model.Presences
import empire.digiprem.projet2_0.feature.calendrier.repository.ICalendrierRepository

/** CU15 (F-CAL-06) — Décompte des présences par camp. */
class GetPresencesUseCase(private val repository: ICalendrierRepository) {
    suspend operator fun invoke(matchId: String): Result<Presences?, CalendrierError> =
        repository.getPresences(matchId).mapFailure { it.toCalendrierError() }
}
