package empire.digiprem.projet2_0.feature.statistiques.datasource

import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.core.data.networking.get
import empire.digiprem.projet2_0.core.data.networking.post
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map
import empire.digiprem.projet2_0.feature.statistiques.dto.BilanSaisonDto
import empire.digiprem.projet2_0.feature.statistiques.dto.LigneClassementDto
import empire.digiprem.projet2_0.feature.statistiques.dto.ProfilJoueurDto
import empire.digiprem.projet2_0.feature.statistiques.dto.TableauSaisonDto
import empire.digiprem.projet2_0.feature.statistiques.dto.VoteHommeDuMatchDto
import empire.digiprem.projet2_0.feature.statistiques.dto.VoterRequest
import empire.digiprem.projet2_0.feature.statistiques.mapper.toDomain
import empire.digiprem.projet2_0.feature.statistiques.model.BilanSaison
import empire.digiprem.projet2_0.feature.statistiques.model.ContexteStats
import empire.digiprem.projet2_0.feature.statistiques.model.LigneClassement
import empire.digiprem.projet2_0.feature.statistiques.model.ProfilJoueur
import empire.digiprem.projet2_0.feature.statistiques.model.TableauSaison
import empire.digiprem.projet2_0.feature.statistiques.model.TypeClassement
import empire.digiprem.projet2_0.feature.statistiques.model.VoteHommeDuMatch
import io.ktor.client.HttpClient

class StatistiquesKtorDataSource(private val client: HttpClient) : IStatistiquesDataSource {

    override suspend fun getTableauSaison(saisonId: String): Result<TableauSaison?, DataError.Remote> =
        client.get<ApiResponseWithPayload<TableauSaisonDto>>(route = "v1/saisons/$saisonId/tableau-saison")
            .map { it.payload?.toDomain() }

    override suspend fun getClassement(saisonId: String, type: TypeClassement): Result<List<LigneClassement>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<LigneClassementDto>>>(route = "v1/saisons/$saisonId/classement?type=${type.name}")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun getProfilJoueur(utilisateurId: String, deuxZeroId: String, contexte: ContexteStats): Result<ProfilJoueur?, DataError.Remote> =
        client.get<ApiResponseWithPayload<ProfilJoueurDto>>(
            route = "v1/utilisateurs/$utilisateurId/profil-joueur?deuxZeroId=$deuxZeroId&contexte=${contexte.name}",
        ).map { it.payload?.toDomain() }

    override suspend fun getVoteHommeDuMatch(matchId: String): Result<VoteHommeDuMatch?, DataError.Remote> =
        client.get<ApiResponseWithPayload<VoteHommeDuMatchDto>>(route = "v1/matchs/$matchId/vote-homme-du-match")
            .map { it.payload?.toDomain() }

    override suspend fun voterHommeDuMatch(matchId: String, candidatId: String): Result<VoteHommeDuMatch?, DataError.Remote> =
        client.post<VoterRequest, ApiResponseWithPayload<VoteHommeDuMatchDto>>(
            route = "v1/matchs/$matchId/voter",
            body = VoterRequest(candidatId),
        ).map { it.payload?.toDomain() }

    override suspend fun getBilanSaison(saisonId: String): Result<BilanSaison?, DataError.Remote> =
        client.get<ApiResponseWithPayload<BilanSaisonDto>>(route = "v1/saisons/$saisonId/bilan")
            .map { it.payload?.toDomain() }
}
