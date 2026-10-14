package empire.digiprem.projet2_0.feature.clubs.create_club

sealed interface CreateClubAction {
    data class OnNomChange(val value: String)         : CreateClubAction
    data class OnQuartierChange(val value: String)     : CreateClubAction
    data class OnDescriptionChange(val value: String)  : CreateClubAction
    data object OnSubmit                                : CreateClubAction
}
