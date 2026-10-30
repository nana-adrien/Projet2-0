package empire.digiprem.projet2_0.contracts.auth

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    val contact: String,
    val purpose: String,
    val code: String,
)

@Serializable
data class VerifyOtpResponse(
    val verificationToken: String,
)
