package empire.digiprem.projet2_0.feature.clubs.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.clubs.error.ClubError
import empire.digiprem.projet2_0.feature.clubs.error.toClubError
import empire.digiprem.projet2_0.feature.clubs.model.Club
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository

/** CU7 (F-CLB-08) — Réglages du club (acceptation automatique, délégation aux capitaines). */
class UpdateClubSettingsUseCase(private val repository: IClubRepository) {
    suspend operator fun invoke(
        clubId: String,
        acceptationAutomatique: Boolean?,
        delegationCapitaines: Boolean?,
    ): Result<Club?, ClubError> =
        repository.updateSettings(clubId, acceptationAutomatique, delegationCapitaines).mapFailure { it.toClubError() }
}
