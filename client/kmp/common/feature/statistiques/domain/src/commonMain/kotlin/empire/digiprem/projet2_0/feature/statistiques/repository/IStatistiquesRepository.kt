package empire.digiprem.projet2_0.feature.statistiques.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.statistiques.model.BilanSaison
import empire.digiprem.projet2_0.feature.statistiques.model.ContexteStats
import empire.digiprem.projet2_0.feature.statistiques.model.LigneClassement
import empire.digiprem.projet2_0.feature.statistiques.model.ProfilJoueur
import empire.digiprem.projet2_0.feature.statistiques.model.TableauSaison
import empire.digiprem.projet2_0.feature.statistiques.model.TypeClassement
import empire.digiprem.projet2_0.feature.statistiques.model.VoteHommeDuMatch

interface IStatistiquesRepository {
    suspend fun getTableauSaison(saisonId: String): Result<TableauSaison?, DataError.Remote>
    suspend fun getClassement(saisonId: String, type: TypeClassement): Result<List<LigneClassement>?, DataError.Remote>
    suspend fun getProfilJoueur(utilisateurId: String, deuxZeroId: String, contexte: ContexteStats): Result<ProfilJoueur?, DataError.Remote>
    suspend fun getVoteHommeDuMatch(matchId: String): Result<VoteHommeDuMatch?, DataError.Remote>
    suspend fun voterHommeDuMatch(matchId: String, candidatId: String): Result<VoteHommeDuMatch?, DataError.Remote>
    suspend fun getBilanSaison(saisonId: String): Result<BilanSaison?, DataError.Remote>
}
