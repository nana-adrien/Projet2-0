package empire.digiprem.projet2_0.feature.calendrier.calendrier

import empire.digiprem.projet2_0.feature.calendrier.model.Match

sealed interface CalendrierAction {
    data class OnInit(val deuxZeroId: String) : CalendrierAction
    data object OnRetry : CalendrierAction
    data class OnFiltreChange(val filtre: CalendrierFiltre) : CalendrierAction
    data object OnGenererCalendrierClick : CalendrierAction
    data object OnAjouterMatchClick : CalendrierAction
    data object OnDismissAjouterMatchDialog : CalendrierAction
    data class OnAjouterMatchConfirm(val date: String, val heure: String, val lieu: String?) : CalendrierAction
    data class OnMatchClick(val match: Match) : CalendrierAction
    data object OnParametresClick : CalendrierAction
}
