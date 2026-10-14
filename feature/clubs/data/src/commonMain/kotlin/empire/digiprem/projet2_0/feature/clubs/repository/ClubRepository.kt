package empire.digiprem.projet2_0.feature.clubs.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.clubs.datasource.IClubDataSource
import empire.digiprem.projet2_0.feature.clubs.model.Club
import empire.digiprem.projet2_0.feature.clubs.model.Membership

class ClubRepository(
    private val dataSource: IClubDataSource,
) : IClubRepository {

    override suspend fun createClub(
        nom: String, logo: String?, couleurs: String?, quartier: String?, description: String?,
    ): Result<Club?, DataError.Remote> = dataSource.createClub(nom, logo, couleurs, quartier, description)

    override suspend fun joinClub(code: String): Result<Membership?, DataError.Remote> =
        dataSource.joinClub(code)

    override suspend fun listMyClubs(): Result<List<Membership>?, DataError.Remote> =
        dataSource.listMyClubs()

    override suspend fun getClub(clubId: String): Result<Club?, DataError.Remote> =
        dataSource.getClub(clubId)

    override suspend fun listMembers(clubId: String): Result<List<Membership>?, DataError.Remote> =
        dataSource.listMembers(clubId)

    override suspend fun acceptMember(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote> =
        dataSource.acceptMember(clubId, targetUserId)

    override suspend fun rejectMember(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote> =
        dataSource.rejectMember(clubId, targetUserId)

    override suspend fun removeMember(clubId: String, targetUserId: String): Result<Unit?, DataError.Remote> =
        dataSource.removeMember(clubId, targetUserId)

    override suspend fun leaveClub(clubId: String): Result<Unit?, DataError.Remote> =
        dataSource.leaveClub(clubId)

    override suspend fun nameCoAdmin(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote> =
        dataSource.nameCoAdmin(clubId, targetUserId)

    override suspend fun transferAdmin(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote> =
        dataSource.transferAdmin(clubId, targetUserId)

    override suspend fun regenerateCode(clubId: String): Result<Club?, DataError.Remote> =
        dataSource.regenerateCode(clubId)

    override suspend fun updateSettings(
        clubId: String, acceptationAutomatique: Boolean?, delegationCapitaines: Boolean?,
    ): Result<Club?, DataError.Remote> = dataSource.updateSettings(clubId, acceptationAutomatique, delegationCapitaines)
}
