package empire.digiprem.projet2_0.feature.validation.litige

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.validation.model.DecisionLitige
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch

/** E16 — Arbitrer un litige (CU20, organisateur du 2-0, décision définitive). */
data class LitigeState(
    val matchId: String = "",
    val deuxZeroId: String = "",
    val campA: Camp? = null,
    val campB: Camp? = null,
    val resultat: ResultatMatch? = null,
    val isLoading: Boolean = true,
    val isActionLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
    val decisionChoisie: DecisionLitige? = null,
    val showMotifDialog: Boolean = false,
) {
    /** Camp qui a proposé la correction (CU19 A1). */
    val correctionProposee get() = resultat?.validations?.firstOrNull { it.correctionProposee != null }

    /** Camp qui a refusé — sa signature CONTESTE sans correction propre, juste un commentaire. */
    val reponseDeRefus get() = resultat?.validations?.firstOrNull { it != correctionProposee }
}
