package empire.digiprem.projet2_0.feature.calendrier.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.calendrier.error.CalendrierError
import empire.digiprem.projet2_0.feature.calendrier.error.toCalendrierError
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.repository.ICalendrierRepository

/** CU15 (F-CAL-05) — Relance manuelle des joueurs n'ayant pas encore répondu. */
class RelancerConvocationsUseCase(private val repository: ICalendrierRepository) {
    suspend operator fun invoke(matchId: String): Result<List<Convocation>?, CalendrierError> =
        repository.relancerConvocations(matchId).mapFailure { it.toCalendrierError() }
}
