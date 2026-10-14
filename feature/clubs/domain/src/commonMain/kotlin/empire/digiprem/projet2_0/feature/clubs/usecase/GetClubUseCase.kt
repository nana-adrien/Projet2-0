package empire.digiprem.projet2_0.feature.clubs.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.clubs.error.ClubError
import empire.digiprem.projet2_0.feature.clubs.error.toClubError
import empire.digiprem.projet2_0.feature.clubs.model.Club
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository

/** E23 — Fiche du club (code, réglages visibles selon le rôle). */
class GetClubUseCase(private val repository: IClubRepository) {
    suspend operator fun invoke(clubId: String): Result<Club?, ClubError> =
        repository.getClub(clubId).mapFailure { it.toClubError() }
}
