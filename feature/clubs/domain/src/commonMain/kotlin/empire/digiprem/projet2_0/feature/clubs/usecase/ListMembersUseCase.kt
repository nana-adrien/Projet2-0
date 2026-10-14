package empire.digiprem.projet2_0.feature.clubs.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.clubs.error.ClubError
import empire.digiprem.projet2_0.feature.clubs.error.toClubError
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository

/** CU7 — Gérer les membres (liste, E23). */
class ListMembersUseCase(private val repository: IClubRepository) {
    suspend operator fun invoke(clubId: String): Result<List<Membership>?, ClubError> =
        repository.listMembers(clubId).mapFailure { it.toClubError() }
}
