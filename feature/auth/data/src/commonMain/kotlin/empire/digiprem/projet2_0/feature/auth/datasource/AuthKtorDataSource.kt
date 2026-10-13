package empire.digiprem.projet2_0.feature.auth.datasource

import empire.digiprem.projet2_0.contracts.base.ApiResponse
import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.core.data.networking.post
import empire.digiprem.projet2_0.core.domain.model.AuthSession
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map
import empire.digiprem.projet2_0.feature.auth.dto.AuthResponse
import empire.digiprem.projet2_0.feature.auth.dto.LoginRequest
import empire.digiprem.projet2_0.feature.auth.dto.LoginWithOtpRequest
import empire.digiprem.projet2_0.feature.auth.dto.RegisterRequest
import empire.digiprem.projet2_0.feature.auth.dto.RequestOtpRequest
import empire.digiprem.projet2_0.feature.auth.dto.ResetPasswordRequest
import empire.digiprem.projet2_0.feature.auth.dto.VerifyOtpRequest
import empire.digiprem.projet2_0.feature.auth.dto.VerifyOtpResponse
import empire.digiprem.projet2_0.feature.auth.mapper.toDomain
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose
import io.ktor.client.HttpClient

class AuthKtorDataSource(private val client: HttpClient) : IAuthDataSource {

    override suspend fun requestOtp(contact: String, purpose: OtpPurpose): Result<Unit?, DataError.Remote> =
        client.post<RequestOtpRequest, ApiResponse>(
            route = "v1/auth/otp",
            body  = RequestOtpRequest(contact, purpose.name),
        ).map { null }

    override suspend fun verifyOtp(
        contact: String,
        purpose: OtpPurpose,
        code   : String,
    ): Result<String?, DataError.Remote> =
        client.post<VerifyOtpRequest, ApiResponseWithPayload<VerifyOtpResponse>>(
            route = "v1/auth/otp/verify",
            body  = VerifyOtpRequest(contact, purpose.name, code),
        ).map { it.payload?.verificationToken }

    override suspend fun register(
        contact          : String,
        pseudo           : String,
        verificationToken: String,
        password         : String?,
        clubCode         : String?,
    ): Result<AuthSession?, DataError.Remote> =
        client.post<RegisterRequest, ApiResponseWithPayload<AuthResponse>>(
            route = "v1/auth/register",
            body  = RegisterRequest(contact, pseudo, verificationToken, password, clubCode),
        ).map { it.payload?.toDomain() }

    override suspend fun login(contact: String, password: String): Result<AuthSession?, DataError.Remote> =
        client.post<LoginRequest, ApiResponseWithPayload<AuthResponse>>(
            route = "v1/auth/login",
            body  = LoginRequest(contact, password),
        ).map { it.payload?.toDomain() }

    override suspend fun loginWithOtp(
        contact          : String,
        verificationToken: String,
    ): Result<AuthSession?, DataError.Remote> =
        client.post<LoginWithOtpRequest, ApiResponseWithPayload<AuthResponse>>(
            route = "v1/auth/login/otp",
            body  = LoginWithOtpRequest(contact, verificationToken),
        ).map { it.payload?.toDomain() }

    override suspend fun resetPassword(
        contact          : String,
        verificationToken: String,
        newPassword      : String,
    ): Result<Unit?, DataError.Remote> =
        client.post<ResetPasswordRequest, ApiResponse>(
            route = "v1/auth/reset-password",
            body  = ResetPasswordRequest(contact, verificationToken, newPassword),
        ).map { null }
}
