package empire.digiprem.projet2_0.contracts.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val contact: String,
    val password: String,
)

@Serializable
data class LoginWithOtpRequest(
    val contact: String,
    val verificationToken: String,
)
