package empire.digiprem.projet2_0.feature.auth.signup_contact

sealed interface SignupContactEvent {
    data class OnOtpSent(val contact: String) : SignupContactEvent
}
