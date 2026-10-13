package empire.digiprem.projet2_0.feature.auth.usecase

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.auth.error.AuthError
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose
import empire.digiprem.projet2_0.feature.auth.repository.IAuthRepository

class RequestOtpUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(contact: String, purpose: OtpPurpose): Result<Unit?, AuthError> =
        repository.requestOtp(contact, purpose)
            .mapFailure { dataError ->
                when (dataError) {
                    DataError.Remote.Conflict -> AuthError.AlreadyExists
                    else -> AuthError.Unknown
                }
            }
}
