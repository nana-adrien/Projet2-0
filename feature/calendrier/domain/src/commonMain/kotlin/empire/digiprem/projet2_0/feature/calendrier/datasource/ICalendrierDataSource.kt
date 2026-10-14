package empire.digiprem.projet2_0.feature.calendrier.datasource

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.Presences
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.calendrier.model.StatutMatchModifiable

interface ICalendrierDataSource {
    suspend fun genererCalendrier(deuxZeroId: String): Result<List<Match>?, DataError.Remote>
    suspend fun ajouterMatchExceptionnel(deuxZeroId: String, date: String, heure: String, lieu: String?): Result<Match?, DataError.Remote>
    suspend fun listMatchs(deuxZeroId: String): Result<List<Match>?, DataError.Remote>
    suspend fun modifierMatch(
        matchId: String,
        nouveauStatut: StatutMatchModifiable,
        nouvelleDate: String?,
        nouvelleHeure: String?,
        motif: String?,
        dureeMiTempsAppliqueeMinutes: Int?,
    ): Result<Match?, DataError.Remote>

    suspend fun genererConvocations(matchId: String): Result<List<Convocation>?, DataError.Remote>
    suspend fun listConvocations(matchId: String): Result<List<Convocation>?, DataError.Remote>
    suspend fun repondreConvocation(convocationId: String, reponse: ReponseConvocation): Result<Convocation?, DataError.Remote>
    suspend fun relancerConvocations(matchId: String): Result<List<Convocation>?, DataError.Remote>
    suspend fun getPresences(matchId: String): Result<Presences?, DataError.Remote>
}
