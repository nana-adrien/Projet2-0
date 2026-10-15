package empire.digiprem.projet2_0.feature.social.datasource

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.social.model.Signalement
import empire.digiprem.projet2_0.feature.social.model.TypeContenuSignale

interface ISocialDataSource {
    suspend fun signaler(contenuType: TypeContenuSignale, contenuId: String, motif: String): Result<Signalement?, DataError.Remote>
}
