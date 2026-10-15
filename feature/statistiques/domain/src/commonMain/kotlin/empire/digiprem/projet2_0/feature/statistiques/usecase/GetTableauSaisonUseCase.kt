package empire.digiprem.projet2_0.feature.statistiques.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.statistiques.error.StatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.error.toStatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.model.TableauSaison
import empire.digiprem.projet2_0.feature.statistiques.repository.IStatistiquesRepository

/** E17 — Tableau de la saison (CU22). */
class GetTableauSaisonUseCase(private val repository: IStatistiquesRepository) {
    suspend operator fun invoke(saisonId: String): Result<TableauSaison?, StatistiquesError> =
        repository.getTableauSaison(saisonId).mapFailure { it.toStatistiquesError() }
}
