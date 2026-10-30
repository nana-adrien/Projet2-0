package empire.digiprem.projet2_0.feature.profile.usecase

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.profile.error.ProfileError
import empire.digiprem.projet2_0.feature.profile.model.Profile
import empire.digiprem.projet2_0.feature.profile.repository.IProfileRepository

/** CU4 — étapes 2 à 4 (modifier pseudo/photo/poste/pied fort/infos, enregistrer). */
class UpdateProfileUseCase(private val repository: IProfileRepository) {
    suspend operator fun invoke(profile: Profile): Result<Profile?, ProfileError> =
        repository.updateProfile(profile).mapFailure { dataError ->
            when (dataError) {
                DataError.Remote.Conflict     -> ProfileError.PseudoDejaPris
                DataError.Remote.Unauthorized,
                DataError.Remote.Forbidden    -> ProfileError.Forbidden
                else                          -> ProfileError.Unknown
            }
        }
}
