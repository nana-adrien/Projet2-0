package empire.digiprem.projet2_0.feature.amical.datasource

import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.core.data.networking.post
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map
import empire.digiprem.projet2_0.feature.amical.dto.CreerMatchAmicalRequest
import empire.digiprem.projet2_0.feature.amical.model.CreerMatchAmicalInput
import empire.digiprem.projet2_0.feature.calendrier.dto.MatchDto
import empire.digiprem.projet2_0.feature.calendrier.mapper.toDomain
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import io.ktor.client.HttpClient

class AmicalKtorDataSource(private val client: HttpClient) : IAmicalDataSource {

    override suspend fun creerMatchAmical(input: CreerMatchAmicalInput): Result<Match?, DataError.Remote> =
        client.post<CreerMatchAmicalRequest, ApiResponseWithPayload<MatchDto>>(
            route = "v1/clubs/${input.clubId}/matchs-amicaux",
            body = CreerMatchAmicalRequest(input.nomEquipeInvitee, input.date, input.heure, input.dureeMiTempsMinutes),
        ).map { it.payload?.toDomain() }
}
