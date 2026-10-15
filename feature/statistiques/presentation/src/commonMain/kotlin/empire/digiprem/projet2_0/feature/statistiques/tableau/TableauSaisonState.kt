package empire.digiprem.projet2_0.feature.statistiques.tableau

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.Saison
import empire.digiprem.projet2_0.feature.statistiques.model.TableauSaison

/** E17 — Tableau de la saison (CU22). */
data class TableauSaisonState(
    val deuxZeroId: String = "",
    val saisons: List<Saison> = emptyList(),
    val saisonSelectionneeId: String = "",
    val campA: Camp? = null,
    val campB: Camp? = null,
    val tableau: TableauSaison? = null,
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
)
