package empire.digiprem.projet2_0.feature.auth.usecase

import empire.digiprem.projet2_0.core.domain.model.AuthSession
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.feature.auth.error.AuthError
import empire.digiprem.projet2_0.feature.auth.repository.IAuthRepository

/** CU1 — Créer un compte (étapes 3 à 7, après validation du code OTP). */
class RegisterUseCase(
    private val repository: IAuthRepository,
    private val authSessionStorage: AuthSessionStorage,
) {
    suspend operator fun invoke(
        contact: String,
        pseudo: String,
        verificationToken: String,
        password: String?,
        clubCode: String?,
    ): Result<AuthSession?, AuthError> =
        repository.register(contact, pseudo, verificationToken, password, clubCode)
            .onSuccess { session -> session?.let { authSessionStorage.set(it) } }
            .mapFailure { dataError ->
                when (dataError) {
                    DataError.Remote.Conflict     -> AuthError.AlreadyExists
                    DataError.Remote.Unauthorized  -> AuthError.InvalidToken
                    else                           -> AuthError.Unknown
                }
            }
}
