package empire.digiprem.projet2_0.feature.clubs.datasource

import empire.digiprem.projet2_0.contracts.base.ApiResponse
import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.core.data.networking.delete
import empire.digiprem.projet2_0.core.data.networking.get
import empire.digiprem.projet2_0.core.data.networking.post
import empire.digiprem.projet2_0.core.data.networking.put
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map
import empire.digiprem.projet2_0.feature.clubs.dto.ClubDto
import empire.digiprem.projet2_0.feature.clubs.dto.CreateClubRequest
import empire.digiprem.projet2_0.feature.clubs.dto.JoinClubRequest
import empire.digiprem.projet2_0.feature.clubs.dto.MembershipDto
import empire.digiprem.projet2_0.feature.clubs.dto.UpdateClubSettingsRequest
import empire.digiprem.projet2_0.feature.clubs.mapper.toDomain
import empire.digiprem.projet2_0.feature.clubs.model.Club
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import io.ktor.client.HttpClient

class ClubKtorDataSource(private val client: HttpClient) : IClubDataSource {

    override suspend fun createClub(
        nom: String,
        logo: String?,
        couleurs: String?,
        quartier: String?,
        description: String?,
    ): Result<Club?, DataError.Remote> =
        client.post<CreateClubRequest, ApiResponseWithPayload<ClubDto>>(
            route = "v1/clubs",
            body = CreateClubRequest(nom, logo, couleurs, quartier, description),
        ).map { it.payload?.toDomain() }

    override suspend fun joinClub(code: String): Result<Membership?, DataError.Remote> =
        client.post<JoinClubRequest, ApiResponseWithPayload<MembershipDto>>(
            route = "v1/clubs/join",
            body = JoinClubRequest(code),
        ).map { it.payload?.toDomain() }

    override suspend fun listMyClubs(): Result<List<Membership>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<MembershipDto>>>(route = "v1/clubs/mine")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun getClub(clubId: String): Result<Club?, DataError.Remote> =
        client.get<ApiResponseWithPayload<ClubDto>>(route = "v1/clubs/$clubId")
            .map { it.payload?.toDomain() }

    override suspend fun listMembers(clubId: String): Result<List<Membership>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<MembershipDto>>>(route = "v1/clubs/$clubId/members")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun acceptMember(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<MembershipDto>>(
            route = "v1/clubs/$clubId/members/$targetUserId/accept",
            body = Unit,
        ).map { it.payload?.toDomain() }

    override suspend fun rejectMember(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<MembershipDto>>(
            route = "v1/clubs/$clubId/members/$targetUserId/reject",
            body = Unit,
        ).map { it.payload?.toDomain() }

    override suspend fun removeMember(clubId: String, targetUserId: String): Result<Unit?, DataError.Remote> =
        client.delete<ApiResponse>(route = "v1/clubs/$clubId/members/$targetUserId").map { null }

    override suspend fun leaveClub(clubId: String): Result<Unit?, DataError.Remote> =
        client.delete<ApiResponse>(route = "v1/clubs/$clubId/membership").map { null }

    override suspend fun nameCoAdmin(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<MembershipDto>>(
            route = "v1/clubs/$clubId/members/$targetUserId/co-admin",
            body = Unit,
        ).map { it.payload?.toDomain() }

    override suspend fun transferAdmin(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<MembershipDto>>(
            route = "v1/clubs/$clubId/members/$targetUserId/transfer-admin",
            body = Unit,
        ).map { it.payload?.toDomain() }

    override suspend fun regenerateCode(clubId: String): Result<Club?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<ClubDto>>(
            route = "v1/clubs/$clubId/regenerate-code",
            body = Unit,
        ).map { it.payload?.toDomain() }

    override suspend fun updateSettings(
        clubId: String,
        acceptationAutomatique: Boolean?,
        delegationCapitaines: Boolean?,
    ): Result<Club?, DataError.Remote> =
        client.put<UpdateClubSettingsRequest, ApiResponseWithPayload<ClubDto>>(
            route = "v1/clubs/$clubId/settings",
            body = UpdateClubSettingsRequest(acceptationAutomatique, delegationCapitaines),
        ).map { it.payload?.toDomain() }
}
