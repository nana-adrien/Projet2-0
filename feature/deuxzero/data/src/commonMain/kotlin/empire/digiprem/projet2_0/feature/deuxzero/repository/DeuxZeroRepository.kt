package empire.digiprem.projet2_0.feature.deuxzero.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.deuxzero.datasource.IDeuxZeroDataSource
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.CreateDeuxZeroInput
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.deuxzero.model.Saison

class DeuxZeroRepository(
    private val dataSource: IDeuxZeroDataSource,
) : IDeuxZeroRepository {

    override suspend fun createDeuxZero(input: CreateDeuxZeroInput): Result<DeuxZero?, DataError.Remote> =
        dataSource.createDeuxZero(input)

    override suspend fun listMesDeuxZero(): Result<List<DeuxZero>?, DataError.Remote> =
        dataSource.listMesDeuxZero()

    override suspend fun engageClub(codeEngagement: String): Result<Camp?, DataError.Remote> =
        dataSource.engageClub(codeEngagement)

    override suspend fun listCamps(deuxZeroId: String): Result<List<Camp>?, DataError.Remote> =
        dataSource.listCamps(deuxZeroId)

    override suspend fun nommerCapitaine(campId: String, utilisateurId: String, viceCapitaine: Boolean): Result<Camp?, DataError.Remote> =
        dataSource.nommerCapitaine(campId, utilisateurId, viceCapitaine)

    override suspend fun transferOrganisateur(deuxZeroId: String, targetUserId: String): Result<DeuxZero?, DataError.Remote> =
        dataSource.transferOrganisateur(deuxZeroId, targetUserId)

    override suspend fun addEffectif(campId: String, utilisateurId: String, numeroMaillot: Int?): Result<Effectif?, DataError.Remote> =
        dataSource.addEffectif(campId, utilisateurId, numeroMaillot)

    override suspend fun listEffectif(campId: String): Result<List<Effectif>?, DataError.Remote> =
        dataSource.listEffectif(campId)

    override suspend fun transferPlayer(utilisateurId: String, nouveauCampId: String): Result<Effectif?, DataError.Remote> =
        dataSource.transferPlayer(utilisateurId, nouveauCampId)

    override suspend fun listSaisons(deuxZeroId: String): Result<List<Saison>?, DataError.Remote> =
        dataSource.listSaisons(deuxZeroId)

    override suspend fun openSaison(deuxZeroId: String, libelle: String, dateDebut: String, dateFin: String?): Result<Saison?, DataError.Remote> =
        dataSource.openSaison(deuxZeroId, libelle, dateDebut, dateFin)

    override suspend fun closeSaison(saisonId: String): Result<Saison?, DataError.Remote> =
        dataSource.closeSaison(saisonId)
}
