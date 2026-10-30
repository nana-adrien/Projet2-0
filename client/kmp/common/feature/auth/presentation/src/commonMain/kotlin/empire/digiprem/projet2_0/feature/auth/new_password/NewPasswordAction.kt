package empire.digiprem.projet2_0.feature.auth.new_password

sealed interface NewPasswordAction {
    data class OnInit(val contact: String, val verificationToken: String) : NewPasswordAction
    data class OnPasswordChange(val value: String)                        : NewPasswordAction
    data object OnToggleVisibility                                        : NewPasswordAction
    data object OnSubmit                                                  : NewPasswordAction
}
