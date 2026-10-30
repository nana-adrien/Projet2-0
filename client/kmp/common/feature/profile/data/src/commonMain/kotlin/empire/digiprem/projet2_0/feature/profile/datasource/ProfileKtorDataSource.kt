package empire.digiprem.projet2_0.feature.profile.datasource

import empire.digiprem.projet2_0.contracts.base.ApiResponse
import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.contracts.profile.ProfileDto
import empire.digiprem.projet2_0.contracts.profile.UpdateProfileRequest
import empire.digiprem.projet2_0.core.data.networking.delete
import empire.digiprem.projet2_0.core.data.networking.get
import empire.digiprem.projet2_0.core.data.networking.put
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map
import empire.digiprem.projet2_0.feature.profile.mapper.toDomain
import empire.digiprem.projet2_0.feature.profile.mapper.toUpdateRequest
import empire.digiprem.projet2_0.feature.profile.model.Profile
import io.ktor.client.HttpClient

class ProfileKtorDataSource(
    private val client: HttpClient,
) : IProfileDataSource {

    override suspend fun getProfile(): Result<Profile?, DataError.Remote> =
        client.get<ApiResponseWithPayload<ProfileDto>>(route = "v1/profile")
            .map { it.payload?.toDomain() }

    override suspend fun updateProfile(profile: Profile): Result<Profile?, DataError.Remote> =
        client.put<UpdateProfileRequest, ApiResponseWithPayload<ProfileDto>>(
            route = "v1/profile",
            body  = profile.toUpdateRequest(),
        ).map { it.payload?.toDomain() }

    override suspend fun deleteAccount(): Result<Unit, DataError.Remote> =
        client.delete<ApiResponse>(route = "v1/profile").map {}
}
