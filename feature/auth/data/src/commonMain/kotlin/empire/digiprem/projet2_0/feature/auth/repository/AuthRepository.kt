package empire.digiprem.projet2_0.feature.auth.repository

import empire.digiprem.projet2_0.core.domain.model.AuthSession
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.auth.datasource.IAuthDataSource
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose

class AuthRepository(
    private val dataSource: IAuthDataSource,
) : IAuthRepository {

    override suspend fun requestOtp(contact: String, purpose: OtpPurpose): Result<Unit?, DataError.Remote> =
        dataSource.requestOtp(contact, purpose)

    override suspend fun verifyOtp(
        contact: String,
        purpose: OtpPurpose,
        code   : String,
    ): Result<String?, DataError.Remote> =
        dataSource.verifyOtp(contact, purpose, code)

    override suspend fun register(
        contact          : String,
        pseudo           : String,
        verificationToken: String,
        password         : String?,
        clubCode         : String?,
    ): Result<AuthSession?, DataError.Remote> =
        dataSource.register(contact, pseudo, verificationToken, password, clubCode)

    override suspend fun login(contact: String, password: String): Result<AuthSession?, DataError.Remote> =
        dataSource.login(contact, password)

    override suspend fun loginWithOtp(
        contact          : String,
        verificationToken: String,
    ): Result<AuthSession?, DataError.Remote> =
        dataSource.loginWithOtp(contact, verificationToken)

    override suspend fun resetPassword(
        contact          : String,
        verificationToken: String,
        newPassword      : String,
    ): Result<Unit?, DataError.Remote> =
        dataSource.resetPassword(contact, verificationToken, newPassword)
}
