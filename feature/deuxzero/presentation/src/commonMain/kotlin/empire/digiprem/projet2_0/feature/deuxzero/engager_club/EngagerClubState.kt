package empire.digiprem.projet2_0.feature.deuxzero.engager_club

import empire.digiprem.projet2_0.core.presentation.extension.UiText

data class EngagerClubState(
    val code: String = "",
    val errorMessage: UiText? = null,
    val isLoading: Boolean = false,
)
