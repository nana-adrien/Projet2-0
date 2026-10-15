package empire.digiprem.projet2_0.feature.statistiques.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.statistiques.error.StatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.error.toStatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.model.BilanSaison
import empire.digiprem.projet2_0.feature.statistiques.repository.IStatistiquesRepository

/** E21 — Bilan de la saison (CU25). */
class GetBilanSaisonUseCase(private val repository: IStatistiquesRepository) {
    suspend operator fun invoke(saisonId: String): Result<BilanSaison?, StatistiquesError> =
        repository.getBilanSaison(saisonId).mapFailure { it.toStatistiquesError() }
}
