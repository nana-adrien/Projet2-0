package empire.digiprem.projet2_0.feature.auth.repository

import empire.digiprem.projet2_0.core.domain.model.AuthSession
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose

interface IAuthRepository {
    suspend fun requestOtp(contact: String, purpose: OtpPurpose): Result<Unit?, DataError.Remote>
    suspend fun verifyOtp(contact: String, purpose: OtpPurpose, code: String): Result<String?, DataError.Remote>
    suspend fun register(
        contact: String,
        pseudo: String,
        verificationToken: String,
        password: String?,
        clubCode: String?,
    ): Result<AuthSession?, DataError.Remote>
    suspend fun login(contact: String, password: String): Result<AuthSession?, DataError.Remote>
    suspend fun loginWithOtp(contact: String, verificationToken: String): Result<AuthSession?, DataError.Remote>
    suspend fun resetPassword(
        contact: String,
        verificationToken: String,
        newPassword: String,
    ): Result<Unit?, DataError.Remote>
}
