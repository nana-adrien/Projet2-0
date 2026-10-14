package empire.digiprem.projet2_0.feature.validation.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.validation.datasource.IValidationDataSource
import empire.digiprem.projet2_0.feature.validation.model.DecisionLitige
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch

class ValidationRepository(
    private val dataSource: IValidationDataSource,
) : IValidationRepository {

    override suspend fun getResultat(matchId: String): Result<ResultatMatch?, DataError.Remote> =
        dataSource.getResultat(matchId)

    override suspend fun valider(matchId: String): Result<ResultatMatch?, DataError.Remote> =
        dataSource.valider(matchId)

    override suspend fun contester(matchId: String, correctionProposee: String, commentaire: String?): Result<ResultatMatch?, DataError.Remote> =
        dataSource.contester(matchId, correctionProposee, commentaire)

    override suspend fun repondreContestation(matchId: String, accepter: Boolean, commentaire: String?): Result<ResultatMatch?, DataError.Remote> =
        dataSource.repondreContestation(matchId, accepter, commentaire)

    override suspend fun trancherLitige(matchId: String, decision: DecisionLitige, motif: String): Result<ResultatMatch?, DataError.Remote> =
        dataSource.trancherLitige(matchId, decision, motif)
}
