package empire.digiprem.projet2_0.feature.profile.usecase

import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.feature.profile.error.ProfileError
import empire.digiprem.projet2_0.feature.profile.repository.IProfileRepository

/** CU4 — A1 : suppression de compte -> anonymisation (RG-20). Déconnecte l'appareil localement. */
class DeleteAccountUseCase(
    private val repository: IProfileRepository,
    private val authSessionStorage: AuthSessionStorage,
) {
    suspend operator fun invoke(): Result<Unit, ProfileError> =
        repository.deleteAccount()
            .onSuccess { authSessionStorage.clear() }
            .mapFailure { ProfileError.Unknown }
}
