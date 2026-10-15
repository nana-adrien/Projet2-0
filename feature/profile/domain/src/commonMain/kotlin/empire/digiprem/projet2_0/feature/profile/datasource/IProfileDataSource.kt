package empire.digiprem.projet2_0.feature.profile.datasource

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.profile.model.Profile

interface IProfileDataSource {
    suspend fun getProfile(): Result<Profile?, DataError.Remote>
    suspend fun updateProfile(profile: Profile): Result<Profile?, DataError.Remote>
    suspend fun deleteAccount(): Result<Unit, DataError.Remote>
}
