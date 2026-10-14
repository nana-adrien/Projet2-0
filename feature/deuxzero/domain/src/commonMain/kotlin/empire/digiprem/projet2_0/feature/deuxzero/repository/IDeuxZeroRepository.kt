package empire.digiprem.projet2_0.feature.deuxzero.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.CreateDeuxZeroInput
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.deuxzero.model.Saison

interface IDeuxZeroRepository {
    suspend fun createDeuxZero(input: CreateDeuxZeroInput): Result<DeuxZero?, DataError.Remote>
    suspend fun listMesDeuxZero(): Result<List<DeuxZero>?, DataError.Remote>
    suspend fun engageClub(codeEngagement: String): Result<Camp?, DataError.Remote>
    suspend fun listCamps(deuxZeroId: String): Result<List<Camp>?, DataError.Remote>
    suspend fun nommerCapitaine(campId: String, utilisateurId: String, viceCapitaine: Boolean): Result<Camp?, DataError.Remote>
    suspend fun transferOrganisateur(deuxZeroId: String, targetUserId: String): Result<DeuxZero?, DataError.Remote>

    suspend fun addEffectif(campId: String, utilisateurId: String, numeroMaillot: Int?): Result<Effectif?, DataError.Remote>
    suspend fun listEffectif(campId: String): Result<List<Effectif>?, DataError.Remote>
    suspend fun transferPlayer(utilisateurId: String, nouveauCampId: String): Result<Effectif?, DataError.Remote>

    suspend fun listSaisons(deuxZeroId: String): Result<List<Saison>?, DataError.Remote>
    suspend fun openSaison(deuxZeroId: String, libelle: String, dateDebut: String, dateFin: String?): Result<Saison?, DataError.Remote>
    suspend fun closeSaison(saisonId: String): Result<Saison?, DataError.Remote>
}
