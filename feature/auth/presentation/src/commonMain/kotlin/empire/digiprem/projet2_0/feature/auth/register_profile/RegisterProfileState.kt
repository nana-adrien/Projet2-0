package empire.digiprem.projet2_0.feature.auth.register_profile

import empire.digiprem.projet2_0.core.presentation.extension.UiText

data class RegisterProfileState(
    val contact: String = "",
    val verificationToken: String = "",
    val pseudo: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val clubCode: String = "",
    val errorMessage: UiText? = null,
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
)
