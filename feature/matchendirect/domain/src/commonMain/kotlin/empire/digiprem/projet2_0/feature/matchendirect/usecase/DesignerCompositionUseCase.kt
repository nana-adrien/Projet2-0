package empire.digiprem.projet2_0.feature.matchendirect.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.matchendirect.error.MatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.error.toMatchEnDirectError
import empire.digiprem.projet2_0.feature.matchendirect.model.Composition
import empire.digiprem.projet2_0.feature.matchendirect.repository.IMatchEnDirectRepository

/** CU16 — Ajouter/mettre à jour un joueur dans la composition (titulaire/remplaçant/présent). */
class DesignerCompositionUseCase(private val repository: IMatchEnDirectRepository) {
    suspend operator fun invoke(matchId: String, utilisateurId: String, titulaire: Boolean, present: Boolean): Result<Composition?, MatchEnDirectError> =
        repository.designerComposition(matchId, utilisateurId, titulaire, present).mapFailure { it.toMatchEnDirectError() }
}
