package empire.digiprem.projet2_0.feature.auth.otp_verification

import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose

sealed interface OtpVerificationAction {
    data class OnInit(val contact: String, val purpose: OtpPurpose) : OtpVerificationAction
    data class OnCodeChange(val value: String)                      : OtpVerificationAction
    data object OnResendClick                                       : OtpVerificationAction
}
