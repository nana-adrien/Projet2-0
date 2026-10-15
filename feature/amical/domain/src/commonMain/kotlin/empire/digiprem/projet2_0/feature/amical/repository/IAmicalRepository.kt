package empire.digiprem.projet2_0.feature.amical.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.amical.model.CreerMatchAmicalInput
import empire.digiprem.projet2_0.feature.calendrier.model.Match

interface IAmicalRepository {
    suspend fun creerMatchAmical(input: CreerMatchAmicalInput): Result<Match?, DataError.Remote>
}
