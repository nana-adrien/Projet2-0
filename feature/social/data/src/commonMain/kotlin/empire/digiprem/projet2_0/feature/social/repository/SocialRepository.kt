package empire.digiprem.projet2_0.feature.social.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.social.datasource.ISocialDataSource
import empire.digiprem.projet2_0.feature.social.model.Signalement
import empire.digiprem.projet2_0.feature.social.model.TypeContenuSignale

class SocialRepository(
    private val dataSource: ISocialDataSource,
) : ISocialRepository {

    override suspend fun signaler(contenuType: TypeContenuSignale, contenuId: String, motif: String): Result<Signalement?, DataError.Remote> =
        dataSource.signaler(contenuType, contenuId, motif)
}
