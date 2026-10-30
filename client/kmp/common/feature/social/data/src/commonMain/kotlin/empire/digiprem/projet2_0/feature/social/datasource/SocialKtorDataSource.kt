package empire.digiprem.projet2_0.feature.social.datasource

import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.core.data.networking.post
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map
import empire.digiprem.projet2_0.feature.social.dto.SignalementDto
import empire.digiprem.projet2_0.feature.social.dto.SignalerRequest
import empire.digiprem.projet2_0.feature.social.mapper.toDomain
import empire.digiprem.projet2_0.feature.social.model.Signalement
import empire.digiprem.projet2_0.feature.social.model.TypeContenuSignale
import io.ktor.client.HttpClient

class SocialKtorDataSource(private val client: HttpClient) : ISocialDataSource {

    override suspend fun signaler(contenuType: TypeContenuSignale, contenuId: String, motif: String): Result<Signalement?, DataError.Remote> =
        client.post<SignalerRequest, ApiResponseWithPayload<SignalementDto>>(
            route = "v1/signalements",
            body = SignalerRequest(contenuType.name, contenuId, motif),
        ).map { it.payload?.toDomain() }
}
