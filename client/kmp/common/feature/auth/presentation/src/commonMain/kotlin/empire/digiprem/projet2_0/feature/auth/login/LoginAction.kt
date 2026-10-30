package empire.digiprem.projet2_0.feature.auth.login

sealed interface LoginAction {
    data class OnContactChange(val value: String)  : LoginAction
    data class OnPasswordChange(val value: String) : LoginAction
    data object OnTogglePasswordVisibility          : LoginAction
    data object OnLoginClick                        : LoginAction
    data object OnLoginWithCodeClick                : LoginAction
    data object OnForgotPasswordClick               : LoginAction
}
