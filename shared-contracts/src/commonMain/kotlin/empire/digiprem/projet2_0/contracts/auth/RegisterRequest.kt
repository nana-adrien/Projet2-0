package empire.digiprem.projet2_0.contracts.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val contact: String,
    val pseudo: String,
    val verificationToken: String,
    val password: String?,
    val clubCode: String?,
)
