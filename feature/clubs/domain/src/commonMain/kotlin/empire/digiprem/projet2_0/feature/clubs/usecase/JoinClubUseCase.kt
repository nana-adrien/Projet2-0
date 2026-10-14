package empire.digiprem.projet2_0.feature.clubs.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.clubs.error.ClubError
import empire.digiprem.projet2_0.feature.clubs.error.toClubError
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository

/** CU6 — Rejoindre un club. */
class JoinClubUseCase(private val repository: IClubRepository) {
    suspend operator fun invoke(code: String): Result<Membership?, ClubError> =
        repository.joinClub(code).mapFailure { it.toClubError() }
}
