package empire.digiprem.projet2_0.feature.calendrier.datasource

import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.core.data.networking.get
import empire.digiprem.projet2_0.core.data.networking.post
import empire.digiprem.projet2_0.core.data.networking.put
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map
import empire.digiprem.projet2_0.feature.calendrier.dto.AjouterMatchExceptionnelRequest
import empire.digiprem.projet2_0.feature.calendrier.dto.ConvocationDto
import empire.digiprem.projet2_0.feature.calendrier.dto.MatchDto
import empire.digiprem.projet2_0.feature.calendrier.dto.ModifierMatchRequest
import empire.digiprem.projet2_0.feature.calendrier.dto.PresencesDto
import empire.digiprem.projet2_0.feature.calendrier.dto.RepondreConvocationRequest
import empire.digiprem.projet2_0.feature.calendrier.mapper.toDomain
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.Presences
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.calendrier.model.StatutMatchModifiable
import io.ktor.client.HttpClient

class CalendrierKtorDataSource(private val client: HttpClient) : ICalendrierDataSource {

    override suspend fun genererCalendrier(deuxZeroId: String): Result<List<Match>?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<List<MatchDto>>>(
            route = "v1/calendrier/$deuxZeroId/generer",
            body = Unit,
        ).map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun ajouterMatchExceptionnel(deuxZeroId: String, date: String, heure: String, lieu: String?): Result<Match?, DataError.Remote> =
        client.post<AjouterMatchExceptionnelRequest, ApiResponseWithPayload<MatchDto>>(
            route = "v1/calendrier/$deuxZeroId/matchs",
            body = AjouterMatchExceptionnelRequest(date, heure, lieu),
        ).map { it.payload?.toDomain() }

    override suspend fun listMatchs(deuxZeroId: String): Result<List<Match>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<MatchDto>>>(route = "v1/calendrier/$deuxZeroId/matchs")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun modifierMatch(
        matchId: String,
        nouveauStatut: StatutMatchModifiable,
        nouvelleDate: String?,
        nouvelleHeure: String?,
        motif: String?,
        dureeMiTempsAppliqueeMinutes: Int?,
    ): Result<Match?, DataError.Remote> =
        client.put<ModifierMatchRequest, ApiResponseWithPayload<MatchDto>>(
            route = "v1/calendrier/matchs/$matchId",
            body = ModifierMatchRequest(nouveauStatut.name, nouvelleDate, nouvelleHeure, motif, dureeMiTempsAppliqueeMinutes),
        ).map { it.payload?.toDomain() }

    override suspend fun genererConvocations(matchId: String): Result<List<Convocation>?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<List<ConvocationDto>>>(
            route = "v1/convocations/matchs/$matchId/generer",
            body = Unit,
        ).map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun listConvocations(matchId: String): Result<List<Convocation>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<ConvocationDto>>>(route = "v1/convocations/matchs/$matchId")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun repondreConvocation(convocationId: String, reponse: ReponseConvocation): Result<Convocation?, DataError.Remote> =
        client.post<RepondreConvocationRequest, ApiResponseWithPayload<ConvocationDto>>(
            route = "v1/convocations/$convocationId/repondre",
            body = RepondreConvocationRequest(reponse.name),
        ).map { it.payload?.toDomain() }

    override suspend fun relancerConvocations(matchId: String): Result<List<Convocation>?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<List<ConvocationDto>>>(
            route = "v1/convocations/matchs/$matchId/relancer",
            body = Unit,
        ).map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun getPresences(matchId: String): Result<Presences?, DataError.Remote> =
        client.get<ApiResponseWithPayload<PresencesDto>>(route = "v1/convocations/matchs/$matchId/presences")
            .map { it.payload?.toDomain() }
}
