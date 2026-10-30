package empire.digiprem.projet2_0.feature.auth.register_profile

sealed interface RegisterProfileAction {
    data class OnInit(val contact: String, val verificationToken: String) : RegisterProfileAction
    data class OnPseudoChange(val value: String)    : RegisterProfileAction
    data class OnPasswordChange(val value: String)  : RegisterProfileAction
    data object OnTogglePasswordVisibility           : RegisterProfileAction
    data class OnClubCodeChange(val value: String)  : RegisterProfileAction
    data object OnSubmit                             : RegisterProfileAction
}
