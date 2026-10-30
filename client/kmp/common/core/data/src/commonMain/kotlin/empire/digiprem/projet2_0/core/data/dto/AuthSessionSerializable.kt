package empire.digiprem.projet2_0.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthSessionSerializable(
    val accessToken  : String = "",
    val refreshToken : String = "",
    val userId       : String = "",
    val pseudo       : String = "",
)
