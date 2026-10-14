package empire.digiprem.projet2_0.feature.calendrier.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.calendrier.error.CalendrierError
import empire.digiprem.projet2_0.feature.calendrier.error.toCalendrierError
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.calendrier.repository.ICalendrierRepository

/** CU15 — Le joueur convoqué répond (présent/absent/incertain) à sa propre convocation. */
class RepondreConvocationUseCase(private val repository: ICalendrierRepository) {
    suspend operator fun invoke(convocationId: String, reponse: ReponseConvocation): Result<Convocation?, CalendrierError> =
        repository.repondreConvocation(convocationId, reponse).mapFailure { it.toCalendrierError() }
}
