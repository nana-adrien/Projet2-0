package empire.digiprem.projet2_0.feature.statistiques.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.statistiques.error.StatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.error.toStatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.model.LigneClassement
import empire.digiprem.projet2_0.feature.statistiques.model.TypeClassement
import empire.digiprem.projet2_0.feature.statistiques.repository.IStatistiquesRepository

/** E18 — Classements des joueurs (CU22). */
class GetClassementUseCase(private val repository: IStatistiquesRepository) {
    suspend operator fun invoke(saisonId: String, type: TypeClassement): Result<List<LigneClassement>?, StatistiquesError> =
        repository.getClassement(saisonId, type).mapFailure { it.toStatistiquesError() }
}
