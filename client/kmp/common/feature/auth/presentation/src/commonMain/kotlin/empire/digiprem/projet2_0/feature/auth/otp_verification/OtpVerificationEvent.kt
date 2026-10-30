package empire.digiprem.projet2_0.feature.auth.otp_verification

sealed interface OtpVerificationEvent {
    data class OnVerifiedForSignup(val contact: String, val verificationToken: String) : OtpVerificationEvent
    data class OnVerifiedForPasswordReset(val contact: String, val verificationToken: String) : OtpVerificationEvent
    data object OnLoggedIn : OtpVerificationEvent
}
