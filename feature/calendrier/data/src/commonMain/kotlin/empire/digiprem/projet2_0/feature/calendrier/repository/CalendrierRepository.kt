package empire.digiprem.projet2_0.feature.calendrier.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.calendrier.datasource.ICalendrierDataSource
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.Presences
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.calendrier.model.StatutMatchModifiable

class CalendrierRepository(
    private val dataSource: ICalendrierDataSource,
) : ICalendrierRepository {

    override suspend fun genererCalendrier(deuxZeroId: String): Result<List<Match>?, DataError.Remote> =
        dataSource.genererCalendrier(deuxZeroId)

    override suspend fun ajouterMatchExceptionnel(deuxZeroId: String, date: String, heure: String, lieu: String?): Result<Match?, DataError.Remote> =
        dataSource.ajouterMatchExceptionnel(deuxZeroId, date, heure, lieu)

    override suspend fun listMatchs(deuxZeroId: String): Result<List<Match>?, DataError.Remote> =
        dataSource.listMatchs(deuxZeroId)

    override suspend fun modifierMatch(
        matchId: String,
        nouveauStatut: StatutMatchModifiable,
        nouvelleDate: String?,
        nouvelleHeure: String?,
        motif: String?,
        dureeMiTempsAppliqueeMinutes: Int?,
    ): Result<Match?, DataError.Remote> =
        dataSource.modifierMatch(matchId, nouveauStatut, nouvelleDate, nouvelleHeure, motif, dureeMiTempsAppliqueeMinutes)

    override suspend fun genererConvocations(matchId: String): Result<List<Convocation>?, DataError.Remote> =
        dataSource.genererConvocations(matchId)

    override suspend fun listConvocations(matchId: String): Result<List<Convocation>?, DataError.Remote> =
        dataSource.listConvocations(matchId)

    override suspend fun repondreConvocation(convocationId: String, reponse: ReponseConvocation): Result<Convocation?, DataError.Remote> =
        dataSource.repondreConvocation(convocationId, reponse)

    override suspend fun relancerConvocations(matchId: String): Result<List<Convocation>?, DataError.Remote> =
        dataSource.relancerConvocations(matchId)

    override suspend fun getPresences(matchId: String): Result<Presences?, DataError.Remote> =
        dataSource.getPresences(matchId)
}
