package empire.digiprem.projet2_0.feature.clubs.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.clubs.error.ClubError
import empire.digiprem.projet2_0.feature.clubs.error.toClubError
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository

/** CU7 — Retirer un membre. */
class RemoveMemberUseCase(private val repository: IClubRepository) {
    suspend operator fun invoke(clubId: String, targetUserId: String): Result<Unit?, ClubError> =
        repository.removeMember(clubId, targetUserId).mapFailure { it.toClubError() }
}
