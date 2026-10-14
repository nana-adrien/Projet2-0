package empire.digiprem.projet2_0.feature.clubs.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.clubs.model.Club
import empire.digiprem.projet2_0.feature.clubs.model.Membership

interface IClubRepository {
    suspend fun createClub(
        nom: String,
        logo: String?,
        couleurs: String?,
        quartier: String?,
        description: String?,
    ): Result<Club?, DataError.Remote>

    suspend fun joinClub(code: String): Result<Membership?, DataError.Remote>
    suspend fun listMyClubs(): Result<List<Membership>?, DataError.Remote>
    suspend fun getClub(clubId: String): Result<Club?, DataError.Remote>
    suspend fun listMembers(clubId: String): Result<List<Membership>?, DataError.Remote>
    suspend fun acceptMember(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote>
    suspend fun rejectMember(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote>
    suspend fun removeMember(clubId: String, targetUserId: String): Result<Unit?, DataError.Remote>
    suspend fun leaveClub(clubId: String): Result<Unit?, DataError.Remote>
    suspend fun nameCoAdmin(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote>
    suspend fun transferAdmin(clubId: String, targetUserId: String): Result<Membership?, DataError.Remote>
    suspend fun regenerateCode(clubId: String): Result<Club?, DataError.Remote>
    suspend fun updateSettings(
        clubId: String,
        acceptationAutomatique: Boolean?,
        delegationCapitaines: Boolean?,
    ): Result<Club?, DataError.Remote>
}
