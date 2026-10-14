package empire.digiprem.projet2_0.feature.clubs.join_club

sealed interface JoinClubAction {
    data class OnCodeChange(val value: String) : JoinClubAction
    data object OnSubmit                        : JoinClubAction
}
