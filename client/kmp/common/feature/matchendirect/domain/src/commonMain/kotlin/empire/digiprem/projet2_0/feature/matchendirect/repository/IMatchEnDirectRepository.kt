package empire.digiprem.projet2_0.feature.matchendirect.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.matchendirect.model.Composition
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement
import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenementSaisissable
import kotlinx.coroutines.flow.Flow

interface IMatchEnDirectRepository {
    suspend fun designerComposition(matchId: String, utilisateurId: String, titulaire: Boolean, present: Boolean): Result<Composition?, DataError.Remote>
    suspend fun listComposition(matchId: String): Result<List<Composition>?, DataError.Remote>
    suspend fun designerMarqueur(matchId: String, utilisateurId: String, suppleant: Boolean): Result<MatchEnDirect?, DataError.Remote>

    suspend fun demarrerMatch(matchId: String): Result<MatchEnDirect?, DataError.Remote>
    suspend fun ajouterEvenement(
        matchId: String,
        type: TypeEvenementSaisissable,
        minute: Int,
        periode: Int,
        campId: String?,
        joueurId: String?,
        joueurSecondaireId: String?,
        clientEventId: String,
    ): Result<MatchEnDirect?, DataError.Remote>
    suspend fun annulerDernierEvenement(matchId: String): Result<MatchEnDirect?, DataError.Remote>
    suspend fun passerMiTemps(matchId: String, periodeTerminee: Int): Result<MatchEnDirect?, DataError.Remote>
    suspend fun terminerMatch(matchId: String, periodeTerminee: Int): Result<MatchEnDirect?, DataError.Remote>
    suspend fun getMatchEnDirect(matchId: String): Result<MatchEnDirect?, DataError.Remote>
    suspend fun listEvenements(matchId: String): Result<List<Evenement>?, DataError.Remote>
    fun observeMatchEnDirect(matchId: String): Flow<MatchEnDirect>
}
