package empire.digiprem.projet2_0.feature.statistiques.profil

import empire.digiprem.projet2_0.feature.statistiques.model.ContexteStats

sealed interface ProfilAction {
    /** [utilisateurId] null = mon propre profil (résolu via la session). */
    data class OnInit(val utilisateurId: String?, val deuxZeroId: String) : ProfilAction
    data object OnRetry : ProfilAction
    data class OnContexteChange(val contexte: ContexteStats) : ProfilAction
}
