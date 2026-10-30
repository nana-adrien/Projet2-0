package empire.digiprem.projet2_0.feature.auth.login

import empire.digiprem.projet2_0.core.presentation.extension.UiText

data class LoginState(
    val contact: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val errorMessage: UiText? = null,
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
)
