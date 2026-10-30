package empire.digiprem.projet2_0.feature.clubs.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.clubs.error.ClubError
import empire.digiprem.projet2_0.feature.clubs.error.toClubError
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository

/** CU5/CU6/CU22 — Mes clubs (E22). */
class ListMyClubsUseCase(private val repository: IClubRepository) {
    suspend operator fun invoke(): Result<List<Membership>?, ClubError> =
        repository.listMyClubs().mapFailure { it.toClubError() }
}
