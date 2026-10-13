package empire.digiprem.projet2_0.feature.auth.usecase

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.auth.error.AuthError
import empire.digiprem.projet2_0.feature.auth.repository.IAuthRepository

class ResetPasswordUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(
        contact          : String,
        verificationToken: String,
        newPassword      : String,
    ): Result<Unit?, AuthError> =
        repository.resetPassword(contact, verificationToken, newPassword)
            .mapFailure { dataError ->
                when (dataError) {
                    DataError.Remote.Unauthorized -> AuthError.InvalidToken
                    DataError.Remote.BadRequest   -> AuthError.WeakPassword
                    else                           -> AuthError.Unknown
                }
            }
}
