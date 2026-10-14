package empire.digiprem.projet2_0.feature.clubs.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.clubs.error.ClubError
import empire.digiprem.projet2_0.feature.clubs.error.toClubError
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository

/** CU7-A1 — Un membre quitte le club de lui-même. */
class LeaveClubUseCase(private val repository: IClubRepository) {
    suspend operator fun invoke(clubId: String): Result<Unit?, ClubError> =
        repository.leaveClub(clubId).mapFailure { it.toClubError() }
}
