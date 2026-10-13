package empire.digiprem.projet2_0.feature.auth.usecase

import empire.digiprem.projet2_0.core.domain.model.AuthSession
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.feature.auth.error.AuthError
import empire.digiprem.projet2_0.feature.auth.repository.IAuthRepository

/** CU2-A1 — Connexion par code SMS/e-mail, sans mot de passe. */
class LoginWithOtpUseCase(
    private val repository: IAuthRepository,
    private val authSessionStorage: AuthSessionStorage,
) {
    suspend operator fun invoke(contact: String, verificationToken: String): Result<AuthSession?, AuthError> =
        repository.loginWithOtp(contact, verificationToken)
            .onSuccess { session -> session?.let { authSessionStorage.set(it) } }
            .mapFailure { dataError ->
                when (dataError) {
                    DataError.Remote.Unauthorized -> AuthError.InvalidToken
                    DataError.Remote.NotFound     -> AuthError.InvalidCredentials
                    else                           -> AuthError.Unknown
                }
            }
}
