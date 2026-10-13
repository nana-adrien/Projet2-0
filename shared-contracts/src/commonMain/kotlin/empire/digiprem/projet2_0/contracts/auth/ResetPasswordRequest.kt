package empire.digiprem.projet2_0.contracts.auth

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val contact: String,
    val verificationToken: String,
    val newPassword: String,
)
