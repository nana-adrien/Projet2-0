package empire.digiprem.projet2_0.feature.profile.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.profile.datasource.IProfileDataSource
import empire.digiprem.projet2_0.feature.profile.model.Profile

class ProfileRepository(
    private val dataSource: IProfileDataSource,
) : IProfileRepository {

    override suspend fun getProfile(): Result<Profile?, DataError.Remote> =
        dataSource.getProfile()

    override suspend fun updateProfile(profile: Profile): Result<Profile?, DataError.Remote> =
        dataSource.updateProfile(profile)

    override suspend fun deleteAccount(): Result<Unit, DataError.Remote> =
        dataSource.deleteAccount()
}
