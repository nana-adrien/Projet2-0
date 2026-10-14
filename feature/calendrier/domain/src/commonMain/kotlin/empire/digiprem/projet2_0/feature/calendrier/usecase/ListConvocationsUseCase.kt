package empire.digiprem.projet2_0.feature.calendrier.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.calendrier.error.CalendrierError
import empire.digiprem.projet2_0.feature.calendrier.error.toCalendrierError
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.repository.ICalendrierRepository

/** CU15 — Liste des convocations d'un match (E08). */
class ListConvocationsUseCase(private val repository: ICalendrierRepository) {
    suspend operator fun invoke(matchId: String): Result<List<Convocation>?, CalendrierError> =
        repository.listConvocations(matchId).mapFailure { it.toCalendrierError() }
}
