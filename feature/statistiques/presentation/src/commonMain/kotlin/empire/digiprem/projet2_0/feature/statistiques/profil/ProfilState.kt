package empire.digiprem.projet2_0.feature.statistiques.profil

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.statistiques.model.ContexteStats
import empire.digiprem.projet2_0.feature.statistiques.model.ProfilJoueur

/** E19 — Profil et progression (CU23). Comparateur (F-PRO-05) hors scope. */
data class ProfilState(
    val utilisateurId: String = "",
    val deuxZeroId: String = "",
    val contexte: ContexteStats = ContexteStats.DEUX_ZERO,
    val profil: ProfilJoueur? = null,
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
)
