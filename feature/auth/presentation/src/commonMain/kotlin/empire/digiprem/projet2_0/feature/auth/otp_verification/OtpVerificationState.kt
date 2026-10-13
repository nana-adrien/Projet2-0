package empire.digiprem.projet2_0.feature.auth.otp_verification

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose

data class OtpVerificationState(
    val contact: String = "",
    val purpose: OtpPurpose = OtpPurpose.SIGNUP,
    val code: String = "",
    val secondsBeforeResend: Int = 60,
    val errorMessage: UiText? = null,
    val isLoading: Boolean = false,
)
