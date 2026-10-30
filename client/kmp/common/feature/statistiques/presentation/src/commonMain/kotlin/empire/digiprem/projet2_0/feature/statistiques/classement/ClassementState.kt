package empire.digiprem.projet2_0.feature.statistiques.classement

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.statistiques.model.LigneClassement
import empire.digiprem.projet2_0.feature.statistiques.model.TypeClassement

/** E18 — Classements des joueurs (CU22). */
data class ClassementState(
    val deuxZeroId: String = "",
    val saisonId: String = "",
    val type: TypeClassement = TypeClassement.BUTEURS,
    val lignes: List<LigneClassement> = emptyList(),
    val currentUserId: String? = null,
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
)
