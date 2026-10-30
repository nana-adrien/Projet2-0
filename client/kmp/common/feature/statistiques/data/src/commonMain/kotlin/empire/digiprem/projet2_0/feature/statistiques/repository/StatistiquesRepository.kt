package empire.digiprem.projet2_0.feature.statistiques.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.statistiques.datasource.IStatistiquesDataSource
import empire.digiprem.projet2_0.feature.statistiques.model.BilanSaison
import empire.digiprem.projet2_0.feature.statistiques.model.ContexteStats
import empire.digiprem.projet2_0.feature.statistiques.model.LigneClassement
import empire.digiprem.projet2_0.feature.statistiques.model.ProfilJoueur
import empire.digiprem.projet2_0.feature.statistiques.model.TableauSaison
import empire.digiprem.projet2_0.feature.statistiques.model.TypeClassement
import empire.digiprem.projet2_0.feature.statistiques.model.VoteHommeDuMatch

class StatistiquesRepository(
    private val dataSource: IStatistiquesDataSource,
) : IStatistiquesRepository {

    override suspend fun getTableauSaison(saisonId: String): Result<TableauSaison?, DataError.Remote> =
        dataSource.getTableauSaison(saisonId)

    override suspend fun getClassement(saisonId: String, type: TypeClassement): Result<List<LigneClassement>?, DataError.Remote> =
        dataSource.getClassement(saisonId, type)

    override suspend fun getProfilJoueur(utilisateurId: String, deuxZeroId: String, contexte: ContexteStats): Result<ProfilJoueur?, DataError.Remote> =
        dataSource.getProfilJoueur(utilisateurId, deuxZeroId, contexte)

    override suspend fun getVoteHommeDuMatch(matchId: String): Result<VoteHommeDuMatch?, DataError.Remote> =
        dataSource.getVoteHommeDuMatch(matchId)

    override suspend fun voterHommeDuMatch(matchId: String, candidatId: String): Result<VoteHommeDuMatch?, DataError.Remote> =
        dataSource.voterHommeDuMatch(matchId, candidatId)

    override suspend fun getBilanSaison(saisonId: String): Result<BilanSaison?, DataError.Remote> =
        dataSource.getBilanSaison(saisonId)
}
