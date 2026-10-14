package empire.digiprem.projet2_0.feature.clubs.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.clubs.error.ClubError
import empire.digiprem.projet2_0.feature.clubs.error.toClubError
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository

/** CU7 (F-CLB-06) — Transférer le rôle d'administrateur. */
class TransferAdminUseCase(private val repository: IClubRepository) {
    suspend operator fun invoke(clubId: String, targetUserId: String): Result<Membership?, ClubError> =
        repository.transferAdmin(clubId, targetUserId).mapFailure { it.toClubError() }
}
