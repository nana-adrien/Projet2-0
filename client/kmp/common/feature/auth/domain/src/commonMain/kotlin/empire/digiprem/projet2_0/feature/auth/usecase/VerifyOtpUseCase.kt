package empire.digiprem.projet2_0.feature.auth.usecase

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.auth.error.AuthError
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose
import empire.digiprem.projet2_0.feature.auth.repository.IAuthRepository

/** Retourne le jeton de vérification à transmettre à l'étape suivante (inscription/connexion/reset). */
class VerifyOtpUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(contact: String, purpose: OtpPurpose, code: String): Result<String?, AuthError> =
        repository.verifyOtp(contact, purpose, code)
            .mapFailure { dataError ->
                when (dataError) {
                    DataError.Remote.TooManyRequests -> AuthError.TooManyAttempts
                    DataError.Remote.Unauthorized     -> AuthError.InvalidCode
                    else                               -> AuthError.Unknown
                }
            }
}
