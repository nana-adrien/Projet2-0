package empire.digiprem.projet2_0.feature.auth.usecase

import empire.digiprem.projet2_0.core.domain.model.AuthSession
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.feature.auth.error.AuthError
import empire.digiprem.projet2_0.feature.auth.repository.IAuthRepository

class LoginUseCase(
    private val repository: IAuthRepository,
    private val authSessionStorage: AuthSessionStorage,
) {
    suspend operator fun invoke(
        contact : String,
        password: String,
    ): Result<AuthSession?, AuthError> =
        repository.login(contact, password)
            .onSuccess { session -> session?.let { authSessionStorage.set(it) } }
            .mapFailure { dataError ->
                when (dataError) {
                    DataError.Remote.Unauthorized -> AuthError.InvalidCredentials
                    DataError.Remote.NotFound     -> AuthError.InvalidCredentials
                    else                           -> AuthError.Unknown
                }
            }
}
