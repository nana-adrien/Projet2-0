package empire.digiprem.projet2_0.feature.clubs.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.clubs.error.ClubError
import empire.digiprem.projet2_0.feature.clubs.error.toClubError
import empire.digiprem.projet2_0.feature.clubs.model.Club
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository

/** CU7 (RG-19) — Régénérer le code d'adhésion (l'ancien devient invalide). */
class RegenerateCodeUseCase(private val repository: IClubRepository) {
    suspend operator fun invoke(clubId: String): Result<Club?, ClubError> =
        repository.regenerateCode(clubId).mapFailure { it.toClubError() }
}
