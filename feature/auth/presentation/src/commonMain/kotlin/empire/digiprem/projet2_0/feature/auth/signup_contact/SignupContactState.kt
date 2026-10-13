package empire.digiprem.projet2_0.feature.auth.signup_contact

import empire.digiprem.projet2_0.core.presentation.extension.UiText

data class SignupContactState(
    val contact: String = "",
    val isEmailMode: Boolean = false,
    val errorMessage: UiText? = null,
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
)
