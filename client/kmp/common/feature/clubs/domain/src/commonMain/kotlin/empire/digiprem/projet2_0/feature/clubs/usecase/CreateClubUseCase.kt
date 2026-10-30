package empire.digiprem.projet2_0.feature.clubs.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.clubs.error.ClubError
import empire.digiprem.projet2_0.feature.clubs.error.toClubError
import empire.digiprem.projet2_0.feature.clubs.model.Club
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository

/** CU5 — Créer un club. */
class CreateClubUseCase(private val repository: IClubRepository) {
    suspend operator fun invoke(
        nom: String,
        logo: String?,
        couleurs: String?,
        quartier: String?,
        description: String?,
    ): Result<Club?, ClubError> =
        repository.createClub(nom, logo, couleurs, quartier, description)
            .mapFailure { it.toClubError() }
}
