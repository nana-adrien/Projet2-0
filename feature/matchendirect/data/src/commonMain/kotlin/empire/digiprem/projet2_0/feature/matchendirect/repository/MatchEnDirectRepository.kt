package empire.digiprem.projet2_0.feature.matchendirect.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.matchendirect.datasource.IMatchEnDirectDataSource
import empire.digiprem.projet2_0.feature.matchendirect.model.Composition
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement
import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenementSaisissable
import kotlinx.coroutines.flow.Flow

class MatchEnDirectRepository(
    private val dataSource: IMatchEnDirectDataSource,
) : IMatchEnDirectRepository {

    override suspend fun designerComposition(matchId: String, utilisateurId: String, titulaire: Boolean, present: Boolean): Result<Composition?, DataError.Remote> =
        dataSource.designerComposition(matchId, utilisateurId, titulaire, present)

    override suspend fun listComposition(matchId: String): Result<List<Composition>?, DataError.Remote> =
        dataSource.listComposition(matchId)

    override suspend fun designerMarqueur(matchId: String, utilisateurId: String, suppleant: Boolean): Result<MatchEnDirect?, DataError.Remote> =
        dataSource.designerMarqueur(matchId, utilisateurId, suppleant)

    override suspend fun demarrerMatch(matchId: String): Result<MatchEnDirect?, DataError.Remote> =
        dataSource.demarrerMatch(matchId)

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
        dataSource.ajouterEvenement(matchId, type, minute, periode, campId, joueurId, joueurSecondaireId, clientEventId)

    override suspend fun annulerDernierEvenement(matchId: String): Result<MatchEnDirect?, DataError.Remote> =
        dataSource.annulerDernierEvenement(matchId)

    override suspend fun passerMiTemps(matchId: String, periodeTerminee: Int): Result<MatchEnDirect?, DataError.Remote> =
        dataSource.passerMiTemps(matchId, periodeTerminee)

    override suspend fun terminerMatch(matchId: String, periodeTerminee: Int): Result<MatchEnDirect?, DataError.Remote> =
        dataSource.terminerMatch(matchId, periodeTerminee)

    override suspend fun getMatchEnDirect(matchId: String): Result<MatchEnDirect?, DataError.Remote> =
        dataSource.getMatchEnDirect(matchId)

    override suspend fun listEvenements(matchId: String): Result<List<Evenement>?, DataError.Remote> =
        dataSource.listEvenements(matchId)

    override fun observeMatchEnDirect(matchId: String): Flow<MatchEnDirect> =
        dataSource.observeMatchEnDirect(matchId)
}
