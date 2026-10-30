package empire.digiprem.projet2_0.feature.auth.new_password

import empire.digiprem.projet2_0.core.presentation.extension.UiText

data class NewPasswordState(
    val contact: String = "",
    val verificationToken: String = "",
    val newPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val errorMessage: UiText? = null,
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
)
