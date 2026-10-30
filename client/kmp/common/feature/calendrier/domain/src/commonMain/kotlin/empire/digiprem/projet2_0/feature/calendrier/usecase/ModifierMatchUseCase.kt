package empire.digiprem.projet2_0.feature.calendrier.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.calendrier.error.CalendrierError
import empire.digiprem.projet2_0.feature.calendrier.error.toCalendrierError
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.StatutMatchModifiable
import empire.digiprem.projet2_0.feature.calendrier.repository.ICalendrierRepository

/** CU14 (F-CAL-03) — Déplacer ou annuler un match (motif obligatoire pour REPORTE/ANNULE). */
class ModifierMatchUseCase(private val repository: ICalendrierRepository) {
    suspend operator fun invoke(
        matchId: String,
        nouveauStatut: StatutMatchModifiable,
        nouvelleDate: String?,
        nouvelleHeure: String?,
        motif: String?,
        dureeMiTempsAppliqueeMinutes: Int? = null,
    ): Result<Match?, CalendrierError> =
        repository.modifierMatch(matchId, nouveauStatut, nouvelleDate, nouvelleHeure, motif, dureeMiTempsAppliqueeMinutes)
            .mapFailure { it.toCalendrierError() }
}
