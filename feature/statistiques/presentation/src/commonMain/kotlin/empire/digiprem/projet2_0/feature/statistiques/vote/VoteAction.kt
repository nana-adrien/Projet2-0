package empire.digiprem.projet2_0.feature.statistiques.vote

sealed interface VoteAction {
    data class OnInit(val matchId: String, val deuxZeroId: String) : VoteAction
    data object OnRetry : VoteAction
    data class OnCandidatClick(val utilisateurId: String) : VoteAction
    data object OnVoterClick : VoteAction
    data object OnMessageShown : VoteAction
}
