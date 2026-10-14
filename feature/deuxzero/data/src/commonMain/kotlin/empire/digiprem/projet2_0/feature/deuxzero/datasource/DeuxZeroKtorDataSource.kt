package empire.digiprem.projet2_0.feature.deuxzero.datasource

import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.core.data.networking.get
import empire.digiprem.projet2_0.core.data.networking.post
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map
import empire.digiprem.projet2_0.feature.deuxzero.dto.AddEffectifRequest
import empire.digiprem.projet2_0.feature.deuxzero.dto.CampDto
import empire.digiprem.projet2_0.feature.deuxzero.dto.CreateDeuxZeroRequest
import empire.digiprem.projet2_0.feature.deuxzero.dto.DeuxZeroDto
import empire.digiprem.projet2_0.feature.deuxzero.dto.EffectifDto
import empire.digiprem.projet2_0.feature.deuxzero.dto.EngageClubRequest
import empire.digiprem.projet2_0.feature.deuxzero.dto.NommerCapitaineRequest
import empire.digiprem.projet2_0.feature.deuxzero.dto.OpenSaisonRequest
import empire.digiprem.projet2_0.feature.deuxzero.dto.SaisonDto
import empire.digiprem.projet2_0.feature.deuxzero.dto.TransferPlayerRequest
import empire.digiprem.projet2_0.feature.deuxzero.mapper.toDomain
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.CreateDeuxZeroInput
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.deuxzero.model.Saison
import io.ktor.client.HttpClient

class DeuxZeroKtorDataSource(private val client: HttpClient) : IDeuxZeroDataSource {

    override suspend fun createDeuxZero(input: CreateDeuxZeroInput): Result<DeuxZero?, DataError.Remote> =
        client.post<CreateDeuxZeroRequest, ApiResponseWithPayload<DeuxZeroDto>>(
            route = "v1/deuxzero",
            body = CreateDeuxZeroRequest(
                nom = input.nom,
                lieu = input.lieu,
                visibilite = input.visibilite.name,
                estInterne = input.estInterne,
                joursHabituels = input.joursHabituels,
                heureHabituelle = input.heureHabituelle,
                dureeMiTempsMinutes = input.dureeMiTempsMinutes,
                nombreMiTemps = input.nombreMiTemps,
                dureePauseMinutes = input.dureePauseMinutes,
                nombreMaxJoueursParCamp = input.nombreMaxJoueursParCamp,
                delaiValidationAutomatiqueHeures = input.delaiValidationAutomatiqueHeures,
                transfertsAutorisesParSaison = input.transfertsAutorisesParSaison,
                libelleCampA = input.libelleCampA,
                libelleCampB = input.libelleCampB,
                libelleSaison = input.libelleSaison,
                dateDebutSaison = input.dateDebutSaison,
            ),
        ).map { it.payload?.toDomain() }

    override suspend fun engageClub(codeEngagement: String): Result<Camp?, DataError.Remote> =
        client.post<EngageClubRequest, ApiResponseWithPayload<CampDto>>(
            route = "v1/deuxzero/engage",
            body = EngageClubRequest(codeEngagement),
        ).map { it.payload?.toDomain() }

    override suspend fun listCamps(deuxZeroId: String): Result<List<Camp>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<CampDto>>>(route = "v1/deuxzero/$deuxZeroId/camps")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun nommerCapitaine(campId: String, utilisateurId: String, viceCapitaine: Boolean): Result<Camp?, DataError.Remote> =
        client.post<NommerCapitaineRequest, ApiResponseWithPayload<CampDto>>(
            route = "v1/deuxzero/camps/$campId/capitaine",
            body = NommerCapitaineRequest(utilisateurId, viceCapitaine),
        ).map { it.payload?.toDomain() }

    override suspend fun transferOrganisateur(deuxZeroId: String, targetUserId: String): Result<DeuxZero?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<DeuxZeroDto>>(
            route = "v1/deuxzero/$deuxZeroId/transfer-organisateur/$targetUserId",
            body = Unit,
        ).map { it.payload?.toDomain() }

    override suspend fun addEffectif(campId: String, utilisateurId: String, numeroMaillot: Int?): Result<Effectif?, DataError.Remote> =
        client.post<AddEffectifRequest, ApiResponseWithPayload<EffectifDto>>(
            route = "v1/effectifs/camps/$campId/membres",
            body = AddEffectifRequest(utilisateurId, numeroMaillot),
        ).map { it.payload?.toDomain() }

    override suspend fun listEffectif(campId: String): Result<List<Effectif>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<EffectifDto>>>(route = "v1/effectifs/camps/$campId/membres")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun transferPlayer(utilisateurId: String, nouveauCampId: String): Result<Effectif?, DataError.Remote> =
        client.post<TransferPlayerRequest, ApiResponseWithPayload<EffectifDto>>(
            route = "v1/effectifs/transfert",
            body = TransferPlayerRequest(utilisateurId, nouveauCampId),
        ).map { it.payload?.toDomain() }

    override suspend fun listSaisons(deuxZeroId: String): Result<List<Saison>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<SaisonDto>>>(route = "v1/deuxzero/$deuxZeroId/saisons")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun openSaison(deuxZeroId: String, libelle: String, dateDebut: String, dateFin: String?): Result<Saison?, DataError.Remote> =
        client.post<OpenSaisonRequest, ApiResponseWithPayload<SaisonDto>>(
            route = "v1/deuxzero/$deuxZeroId/saisons",
            body = OpenSaisonRequest(libelle, dateDebut, dateFin),
        ).map { it.payload?.toDomain() }

    override suspend fun closeSaison(saisonId: String): Result<Saison?, DataError.Remote> =
        client.post<Unit, ApiResponseWithPayload<SaisonDto>>(
            route = "v1/deuxzero/saisons/$saisonId/cloturer",
            body = Unit,
        ).map { it.payload?.toDomain() }
}
