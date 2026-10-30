package empire.digiprem.projet2_0.feature.matchendirect.datasource

import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.core.data.networking.HttpConstants
import empire.digiprem.projet2_0.core.data.networking.get
import empire.digiprem.projet2_0.core.data.networking.post
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map
import empire.digiprem.projet2_0.feature.matchendirect.dto.AjouterEvenementRequest
import empire.digiprem.projet2_0.feature.matchendirect.dto.CompositionDto
import empire.digiprem.projet2_0.feature.matchendirect.dto.DesignerCompositionRequest
import empire.digiprem.projet2_0.feature.matchendirect.dto.DesignerMarqueurRequest
import empire.digiprem.projet2_0.feature.matchendirect.dto.EvenementDto
import empire.digiprem.projet2_0.feature.matchendirect.dto.MatchEnDirectDto
import empire.digiprem.projet2_0.feature.matchendirect.dto.PeriodeRequest
import empire.digiprem.projet2_0.feature.matchendirect.mapper.toDomain
import empire.digiprem.projet2_0.feature.matchendirect.model.Composition
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement
import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenementSaisissable
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class MatchEnDirectKtorDataSource(
    private val client: HttpClient,
    private val authSessionStorage: AuthSessionStorage,
) : IMatchEnDirectDataSource {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun designerComposition(matchId: String, utilisateurId: String, titulaire: Boolean, present: Boolean): Result<Composition?, DataError.Remote> =
        client.post<DesignerCompositionRequest, ApiResponseWithPayload<CompositionDto>>(
            route = "v1/matchs/$matchId/composition",
            body = DesignerCompositionRequest(utilisateurId, titulaire, present),
        ).map { it.payload?.toDomain() }

    override suspend fun listComposition(matchId: String): Result<List<Composition>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<CompositionDto>>>(route = "v1/matchs/$matchId/composition")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun designerMarqueur(matchId: String, utilisateurId: String, suppleant: Boolean): Result<MatchEnDirect?, DataError.Remote> =
        client.post<DesignerMarqueurRequest, ApiResponseWithPayload<MatchEnDirectDto>>(
            route = "v1/matchs/$matchId/marqueur",
            body = DesignerMarqueurRequest(utilisateurId, suppleant),
        ).map { it.payload?.toDomain() }

    override suspend fun demarrerMatch(matchId: String): Result<MatchEnDirect?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<MatchEnDirectDto>>(
            route = "v1/matchs/$matchId/demarrer",
            body = Unit,
        ).map { it.payload?.toDomain() }

    override suspend fun ajouterEvenement(
        matchId: String,
        type: TypeEvenementSaisissable,
        minute: Int,
        periode: Int,
        campId: String?,
        joueurId: String?,
        joueurSecondaireId: String?,
        clientEventId: String,
    ): Result<MatchEnDirect?, DataError.Remote> =
        client.post<AjouterEvenementRequest, ApiResponseWithPayload<MatchEnDirectDto>>(
            route = "v1/matchs/$matchId/evenements",
            body = AjouterEvenementRequest(type.name, minute, periode, campId, joueurId, joueurSecondaireId, clientEventId),
        ).map { it.payload?.toDomain() }

    override suspend fun annulerDernierEvenement(matchId: String): Result<MatchEnDirect?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<MatchEnDirectDto>>(
            route = "v1/matchs/$matchId/evenements/annuler-dernier",
            body = Unit,
        ).map { it.payload?.toDomain() }

    override suspend fun passerMiTemps(matchId: String, periodeTerminee: Int): Result<MatchEnDirect?, DataError.Remote> =
        client.post<PeriodeRequest, ApiResponseWithPayload<MatchEnDirectDto>>(
            route = "v1/matchs/$matchId/mi-temps",
            body = PeriodeRequest(periodeTerminee),
        ).map { it.payload?.toDomain() }

    override suspend fun terminerMatch(matchId: String, periodeTerminee: Int): Result<MatchEnDirect?, DataError.Remote> =
        client.post<PeriodeRequest, ApiResponseWithPayload<MatchEnDirectDto>>(
            route = "v1/matchs/$matchId/terminer",
            body = PeriodeRequest(periodeTerminee),
        ).map { it.payload?.toDomain() }

    override suspend fun getMatchEnDirect(matchId: String): Result<MatchEnDirect?, DataError.Remote> =
        client.get<ApiResponseWithPayload<MatchEnDirectDto>>(route = "v1/matchs/$matchId/en-direct")
            .map { it.payload?.toDomain() }

    override suspend fun listEvenements(matchId: String): Result<List<Evenement>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<EvenementDto>>>(route = "v1/matchs/$matchId/evenements")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override fun observeMatchEnDirect(matchId: String): Flow<MatchEnDirect> = flow {
        val accessToken = authSessionStorage.getData()?.accessToken
        val session = client.webSocketSession(urlString = HttpConstants.WS_BASE_URL + "matchs/$matchId") {
            accessToken?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
        try {
            for (frame in session.incoming) {
                if (frame is Frame.Text) {
                    val dto = json.decodeFromString(MatchEnDirectDto.serializer(), frame.readText())
                    emit(dto.toDomain())
                }
            }
        } catch (e: ClosedReceiveChannelException) {
            // Fermeture normale du socket — on laisse le Flow se terminer sans erreur.
        } finally {
            session.close()
        }
    }
}
