package empire.digiprem.projet2_0.feature.clubs.join_club

import empire.digiprem.projet2_0.core.presentation.extension.UiText

data class JoinClubState(
    val code: String = "",
    val errorMessage: UiText? = null,
    val isLoading: Boolean = false,
)
