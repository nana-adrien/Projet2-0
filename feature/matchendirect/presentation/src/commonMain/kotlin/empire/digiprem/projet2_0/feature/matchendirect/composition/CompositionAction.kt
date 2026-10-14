package empire.digiprem.projet2_0.feature.matchendirect.composition

import empire.digiprem.projet2_0.feature.deuxzero.model.Camp

sealed interface CompositionAction {
    data class OnInit(val camp: Camp, val matchId: String) : CompositionAction
    data object OnRetry : CompositionAction
    data class OnToggleTitulaire(val utilisateurId: String) : CompositionAction
    data class OnToggleRemplacant(val utilisateurId: String) : CompositionAction
    data object OnChangerMarqueurClick : CompositionAction
    data object OnChangerSuppleantClick : CompositionAction
    data object OnDismissPicker : CompositionAction
    data class OnSelectMarqueur(val utilisateurId: String) : CompositionAction
    data class OnSelectSuppleant(val utilisateurId: String) : CompositionAction
    data object OnMessageShown : CompositionAction
    data object OnDoneClick : CompositionAction
}
