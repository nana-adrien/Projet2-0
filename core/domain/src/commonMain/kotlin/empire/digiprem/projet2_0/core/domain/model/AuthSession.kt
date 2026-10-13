package empire.digiprem.projet2_0.core.domain.model

data class AuthSession(
    val accessToken  : String,
    val refreshToken : String,
    val userId       : String,
    val pseudo       : String,
)
