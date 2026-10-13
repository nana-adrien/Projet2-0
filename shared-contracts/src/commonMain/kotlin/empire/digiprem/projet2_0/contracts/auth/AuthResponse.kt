package empire.digiprem.projet2_0.contracts.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val pseudo: String,
)
