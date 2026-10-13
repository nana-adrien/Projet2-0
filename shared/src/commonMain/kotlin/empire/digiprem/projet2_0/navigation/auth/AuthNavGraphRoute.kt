package empire.digiprem.projet2_0.navigation.auth

import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose
import kotlinx.serialization.Serializable

sealed interface AuthNavGraphRoute {
    @Serializable data object Auth : AuthNavGraphRoute
    @Serializable data object Welcome : AuthNavGraphRoute
    @Serializable data object Login : AuthNavGraphRoute
    @Serializable data object SignupContact : AuthNavGraphRoute
    @Serializable data class OtpVerification(val contact: String, val purpose: OtpPurpose) : AuthNavGraphRoute
    @Serializable data class RegisterProfile(val contact: String, val verificationToken: String) : AuthNavGraphRoute
    @Serializable data class NewPassword(val contact: String, val verificationToken: String) : AuthNavGraphRoute
}
