package empire.digiprem.projet2_0.feature.matchendirect.suivre

sealed interface SuivreEnDirectAction {
    data class OnInit(val matchId: String, val deuxZeroId: String) : SuivreEnDirectAction
    data object OnRetry : SuivreEnDirectAction
    data class OnTabChange(val tab: SuivreEnDirectTab) : SuivreEnDirectAction
}
