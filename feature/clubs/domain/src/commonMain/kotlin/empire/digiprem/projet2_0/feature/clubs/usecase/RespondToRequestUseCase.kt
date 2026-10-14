package empire.digiprem.projet2_0.feature.clubs.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.clubs.error.ClubError
import empire.digiprem.projet2_0.feature.clubs.error.toClubError
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository

/** CU7 — Accepter/refuser une demande d'adhésion. */
class RespondToRequestUseCase(private val repository: IClubRepository) {
    suspend operator fun invoke(clubId: String, targetUserId: String, accept: Boolean): Result<Membership?, ClubError> =
        if (accept) repository.acceptMember(clubId, targetUserId).mapFailure { it.toClubError() }
        else repository.rejectMember(clubId, targetUserId).mapFailure { it.toClubError() }
}
