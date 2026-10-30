package empire.digiprem.projet2_0.feature.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class RequestOtpRequest(
    val contact: String,
    val purpose: String,
)

@Serializable
data class VerifyOtpRequest(
    val contact: String,
    val purpose: String,
    val code: String,
)

@Serializable
data class VerifyOtpResponse(val verificationToken: String = "")

@Serializable
data class RegisterRequest(
    val contact: String,
    val pseudo: String,
    val verificationToken: String,
    val password: String? = null,
    val clubCode: String? = null,
)

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

@Serializable
data class ResetPasswordRequest(
    val contact: String,
    val verificationToken: String,
    val newPassword: String,
)

@Serializable
data class AuthResponse(
    val accessToken  : String = "",
    val refreshToken : String = "",
    val userId       : String = "",
    val pseudo       : String = "",
)
