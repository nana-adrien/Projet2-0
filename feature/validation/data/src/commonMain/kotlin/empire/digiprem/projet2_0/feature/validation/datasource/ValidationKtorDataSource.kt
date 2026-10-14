package empire.digiprem.projet2_0.feature.validation.datasource

import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.core.data.networking.get
import empire.digiprem.projet2_0.core.data.networking.post
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map
import empire.digiprem.projet2_0.feature.validation.dto.ContesterRequest
import empire.digiprem.projet2_0.feature.validation.dto.RepondreContestationRequest
import empire.digiprem.projet2_0.feature.validation.dto.ResultatMatchDto
import empire.digiprem.projet2_0.feature.validation.dto.TrancherLitigeRequest
import empire.digiprem.projet2_0.feature.validation.mapper.toDomain
import empire.digiprem.projet2_0.feature.validation.model.DecisionLitige
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch
import io.ktor.client.HttpClient

class ValidationKtorDataSource(private val client: HttpClient) : IValidationDataSource {

    override suspend fun getResultat(matchId: String): Result<ResultatMatch?, DataError.Remote> =
        client.get<ApiResponseWithPayload<ResultatMatchDto>>(route = "v1/matchs/$matchId/resultat-a-valider")
            .map { it.payload?.toDomain() }

    override suspend fun valider(matchId: String): Result<ResultatMatch?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<ResultatMatchDto>>(
            route = "v1/matchs/$matchId/valider",
            body = Unit,
        ).map { it.payload?.toDomain() }

    override suspend fun contester(matchId: String, correctionProposee: String, commentaire: String?): Result<ResultatMatch?, DataError.Remote> =
        client.post<ContesterRequest, ApiResponseWithPayload<ResultatMatchDto>>(
            route = "v1/matchs/$matchId/contester",
            body = ContesterRequest(correctionProposee, commentaire),
        ).map { it.payload?.toDomain() }

    override suspend fun repondreContestation(matchId: String, accepter: Boolean, commentaire: String?): Result<ResultatMatch?, DataError.Remote> =
        client.post<RepondreContestationRequest, ApiResponseWithPayload<ResultatMatchDto>>(
            route = "v1/matchs/$matchId/repondre-contestation",
            body = RepondreContestationRequest(accepter, commentaire),
        ).map { it.payload?.toDomain() }

    override suspend fun trancherLitige(matchId: String, decision: DecisionLitige, motif: String): Result<ResultatMatch?, DataError.Remote> =
        client.post<TrancherLitigeRequest, ApiResponseWithPayload<ResultatMatchDto>>(
            route = "v1/matchs/$matchId/trancher-litige",
            body = TrancherLitigeRequest(decision.name, motif),
        ).map { it.payload?.toDomain() }
}
