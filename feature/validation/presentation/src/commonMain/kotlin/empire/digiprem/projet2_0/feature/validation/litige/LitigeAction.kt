package empire.digiprem.projet2_0.feature.validation.litige

import empire.digiprem.projet2_0.feature.validation.model.DecisionLitige

sealed interface LitigeAction {
    data class OnInit(val matchId: String, val deuxZeroId: String) : LitigeAction
    data object OnRetry : LitigeAction
    data class OnDecisionClick(val decision: DecisionLitige) : LitigeAction
    data object OnDismissMotifDialog : LitigeAction
    data class OnConfirmerDecision(val motif: String) : LitigeAction
    data object OnMessageShown : LitigeAction
}
