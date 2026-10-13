package empire.digiprem.projet2_0.server.user.api

import empire.digiprem.projet2_0.contracts.auth.LoginRequest
import empire.digiprem.projet2_0.contracts.auth.LoginWithOtpRequest
import empire.digiprem.projet2_0.contracts.auth.RefreshTokenRequest
import empire.digiprem.projet2_0.contracts.auth.RegisterRequest
import empire.digiprem.projet2_0.contracts.auth.RequestOtpRequest
import empire.digiprem.projet2_0.contracts.auth.ResetPasswordRequest
import empire.digiprem.projet2_0.contracts.auth.VerifyOtpRequest
import empire.digiprem.projet2_0.contracts.base.ApiResponse
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.user.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth", name = "Auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/otp")
    fun requestOtp(@RequestBody body: RequestOtpRequest): ApiResponse {
        authService.requestOtp(body)
        return ApiResponse.ok()
    }

    @PostMapping("/otp/verify")
    fun verifyOtp(@RequestBody body: VerifyOtpRequest) = authService.verifyOtp(body).toApiResponse()

    @PostMapping("/register")
    fun register(@RequestBody body: RegisterRequest) = authService.register(body).toApiResponse()

    @PostMapping("/login")
    fun login(@RequestBody body: LoginRequest) = authService.login(body).toApiResponse()

    @PostMapping("/login/otp")
    fun loginWithOtp(@RequestBody body: LoginWithOtpRequest) = authService.loginWithOtp(body).toApiResponse()

    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody body: ResetPasswordRequest): ApiResponse {
        authService.resetPassword(body)
        return ApiResponse.ok()
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody body: RefreshTokenRequest) = authService.refresh(body.refreshToken).toApiResponse()

    @PostMapping("/logout")
    fun logout(@RequestBody body: RefreshTokenRequest): ApiResponse {
        authService.logout(body.refreshToken)
        return ApiResponse.ok()
    }
}
