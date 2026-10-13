package empire.digiprem.projet2_0.feature.auth.signup_contact

sealed interface SignupContactAction {
    data class OnContactChange(val value: String) : SignupContactAction
    data object OnToggleEmailMode                  : SignupContactAction
    data object OnSubmit                           : SignupContactAction
}
