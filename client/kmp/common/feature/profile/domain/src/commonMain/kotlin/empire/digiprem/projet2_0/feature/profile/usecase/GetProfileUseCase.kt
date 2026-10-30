package empire.digiprem.projet2_0.feature.profile.usecase

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.profile.error.ProfileError
import empire.digiprem.projet2_0.feature.profile.model.Profile
import empire.digiprem.projet2_0.feature.profile.repository.IProfileRepository

class GetProfileUseCase(private val repository: IProfileRepository) {
    suspend operator fun invoke(): Result<Profile?, ProfileError> =
        repository.getProfile().mapFailure { dataError ->
            when (dataError) {
                DataError.Remote.NotFound     -> ProfileError.NotFound
                DataError.Remote.Unauthorized,
                DataError.Remote.Forbidden    -> ProfileError.Forbidden
                else                          -> ProfileError.Unknown
            }
        }
}
