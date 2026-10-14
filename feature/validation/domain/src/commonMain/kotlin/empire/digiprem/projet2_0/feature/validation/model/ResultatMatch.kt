package empire.digiprem.projet2_0.feature.validation.model

import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement

/** CU19/CU20 — récapitulatif complet d'un match à valider ou en litige. */
data class ResultatMatch(
    val match: Match,
    val evenements: List<Evenement>,
    val validations: List<Validation>,
) {
    fun validationDuCamp(campId: String): Validation? = validations.firstOrNull { it.campId == campId }

    fun aUneContestationNonTraitee(monCampId: String): Boolean {
        val contestationAdverse = validations.firstOrNull { it.campId != monCampId && it.decision == DecisionValidation.CONTESTE }
        val maReponse = validationDuCamp(monCampId)
        return contestationAdverse != null && maReponse == null
    }

    fun contestationAdverse(monCampId: String): Validation? =
        validations.firstOrNull { it.campId != monCampId && it.decision == DecisionValidation.CONTESTE }
}

enum class DecisionLitige { GARDER_RESULTAT_SAISI, APPLIQUER_CORRECTION, ANNULER_MATCH }
