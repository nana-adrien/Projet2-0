package empire.digiprem.projet2_0.feature.amical.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.amical.error.AmicalError
import empire.digiprem.projet2_0.feature.amical.error.toAmicalError
import empire.digiprem.projet2_0.feature.amical.model.CreerMatchAmicalInput
import empire.digiprem.projet2_0.feature.amical.repository.IAmicalRepository
import empire.digiprem.projet2_0.feature.calendrier.model.Match

/** E26 — Créer et convoquer un match amical (CU21, RG-14/15). */
class CreerMatchAmicalUseCase(private val repository: IAmicalRepository) {
    suspend operator fun invoke(input: CreerMatchAmicalInput): Result<Match?, AmicalError> =
        repository.creerMatchAmical(input).mapFailure { it.toAmicalError() }
}
