package empire.digiprem.projet2_0.feature.validation.datasource

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.validation.model.DecisionLitige
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch

interface IValidationDataSource {
    suspend fun getResultat(matchId: String): Result<ResultatMatch?, DataError.Remote>
    suspend fun valider(matchId: String): Result<ResultatMatch?, DataError.Remote>
    suspend fun contester(matchId: String, correctionProposee: String, commentaire: String?): Result<ResultatMatch?, DataError.Remote>
    suspend fun repondreContestation(matchId: String, accepter: Boolean, commentaire: String?): Result<ResultatMatch?, DataError.Remote>
    suspend fun trancherLitige(matchId: String, decision: DecisionLitige, motif: String): Result<ResultatMatch?, DataError.Remote>
}
