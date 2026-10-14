package empire.digiprem.projet2_0.feature.deuxzero.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.error.toDeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository

/** CU9 — Engager un club dans un 2-0 via son code d'engagement. */
class EngageClubUseCase(private val repository: IDeuxZeroRepository) {
    suspend operator fun invoke(codeEngagement: String): Result<Camp?, DeuxZeroError> =
        repository.engageClub(codeEngagement).mapFailure { it.toDeuxZeroError() }
}
