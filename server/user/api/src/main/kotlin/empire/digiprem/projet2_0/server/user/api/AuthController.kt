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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth", name = "Auth")
@Tag(name = "Authentification", description = "Inscription, connexion, codes OTP et gestion de la session")
class AuthController(
    private val authService: AuthService,
) {

    @Operation(
        summary = "Demander un code de vérification",
        description = "Envoie un code à usage unique par SMS ou e-mail. `purpose` accepte SIGNUP, LOGIN ou PASSWORD_RESET (F-CPT-01, F-CPT-03)."
    )
    @PostMapping("/otp")
    fun requestOtp(@RequestBody body: RequestOtpRequest): ApiResponse {
        authService.requestOtp(body)
        return ApiResponse.ok()
    }

    @Operation(
        summary = "Vérifier un code OTP",
        description = "Contrôle le code reçu et renvoie un jeton de vérification à utiliser pour l'inscription, la connexion par OTP ou la réinitialisation du mot de passe."
    )
    @PostMapping("/otp/verify")
    fun verifyOtp(@RequestBody body: VerifyOtpRequest) = authService.verifyOtp(body).toApiResponse()

    @Operation(
        summary = "Créer un compte",
        description = "Inscrit un joueur après vérification du contact. Un code d'adhésion de club facultatif déclenche immédiatement une demande d'adhésion (F-CPT-01, F-CPT-04)."
    )
    @PostMapping("/register")
    fun register(@RequestBody body: RegisterRequest) = authService.register(body).toApiResponse()

    @Operation(
        summary = "Se connecter par mot de passe",
        description = "Authentifie le joueur par son contact et son mot de passe, puis renvoie un jeton d'accès et un jeton de rafraîchissement (F-CPT-02)."
    )
    @PostMapping("/login")
    fun login(@RequestBody body: LoginRequest) = authService.login(body).toApiResponse()

    @Operation(
        summary = "Se connecter par code OTP",
        description = "Authentifie le joueur sans mot de passe à partir du jeton de vérification OTP (F-CPT-02)."
    )
    @PostMapping("/login/otp")
    fun loginWithOtp(@RequestBody body: LoginWithOtpRequest) = authService.loginWithOtp(body).toApiResponse()

    @Operation(
        summary = "Réinitialiser le mot de passe",
        description = "Remplace le mot de passe après vérification du code envoyé par SMS ou e-mail (F-CPT-03)."
    )
    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody body: ResetPasswordRequest): ApiResponse {
        authService.resetPassword(body)
        return ApiResponse.ok()
    }

    @Operation(
        summary = "Rafraîchir la session",
        description = "Échange un jeton de rafraîchissement valide contre un nouveau couple de jetons d'accès et de rafraîchissement."
    )
    @PostMapping("/refresh")
    fun refresh(@RequestBody body: RefreshTokenRequest) = authService.refresh(body.refreshToken).toApiResponse()

    @Operation(
        summary = "Se déconnecter",
        description = "Invalide le jeton de rafraîchissement fourni et met fin à la session sur l'appareil."
    )
    @PostMapping("/logout")
    fun logout(@RequestBody body: RefreshTokenRequest): ApiResponse {
        authService.logout(body.refreshToken)
        return ApiResponse.ok()
    }
}
