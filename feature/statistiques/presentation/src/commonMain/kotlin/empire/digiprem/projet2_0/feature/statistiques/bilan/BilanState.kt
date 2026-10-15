package empire.digiprem.projet2_0.feature.statistiques.bilan

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.statistiques.model.BilanSaison

/** E21 — Bilan de la saison (CU25). Visuel partageable (F-BIL-05) et palmarès historique
 * multi-saisons (F-BIL-06) hors scope. */
data class BilanState(
    val saisonId: String = "",
    val deuxZeroId: String = "",
    val campA: Camp? = null,
    val campB: Camp? = null,
    val bilan: BilanSaison? = null,
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
) {
    val campChampionLibelle: String?
        get() = when (bilan?.campChampionId) {
            campA?.id -> campA?.libelle
            campB?.id -> campB?.libelle
            else -> null
        }
}
