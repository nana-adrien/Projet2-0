package empire.digiprem.projet2_0.feature.deuxzero.engager_club

sealed interface EngagerClubAction {
    data class OnCodeChange(val value: String) : EngagerClubAction
    data object OnSubmit                        : EngagerClubAction
}
