package empire.digiprem.projet2_0.feature.auth.login

import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose

sealed interface LoginEvent {
    data object OnLoginSuccess : LoginEvent
    data class OnOtpRequested(val contact: String, val purpose: OtpPurpose) : LoginEvent
}
