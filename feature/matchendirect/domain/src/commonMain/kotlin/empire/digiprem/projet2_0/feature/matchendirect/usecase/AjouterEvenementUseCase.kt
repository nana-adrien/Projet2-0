package empire.digiprem.projet2_0.feature.matchendirect.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.matchendirect.error.MatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.error.toMatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenementSaisissable
import empire.digiprem.projet2_0.feature.matchendirect.repository.IMatchEnDirectRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/** CU17 (F-LIV-03/F-EFF-*) — ajoute un événement de la feuille de match. [clientEventId] est
 * généré ici (pas par l'appelant) : identifiant stable pour la resynchronisation hors connexion
 * (F-LIV-06) — le serveur ignore les doublons sur ce champ. */
class AjouterEvenementUseCase(private val repository: IMatchEnDirectRepository) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        matchId: String,
        type: TypeEvenementSaisissable,
        minute: Int,
        periode: Int,
        campId: String?,
        joueurId: String?,
        joueurSecondaireId: String?,
    ): Result<MatchEnDirect?, MatchEnDirectError> =
        repository.ajouterEvenement(matchId, type, minute, periode, campId, joueurId, joueurSecondaireId, Uuid.random().toString())
            .mapFailure { it.toMatchEnDirectError() }
}
