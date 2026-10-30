package empire.digiprem.projet2_0.feature.clubs.create_club

import empire.digiprem.projet2_0.core.presentation.extension.UiText

data class CreateClubState(
    val nom: String = "",
    val quartier: String = "",
    val description: String = "",
    val errorMessage: UiText? = null,
    val isLoading: Boolean = false,
)
