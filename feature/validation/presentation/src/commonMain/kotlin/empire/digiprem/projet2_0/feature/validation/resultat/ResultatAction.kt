package empire.digiprem.projet2_0.feature.validation.resultat

import empire.digiprem.projet2_0.feature.deuxzero.model.Camp

sealed interface ResultatAction {
    data class OnInit(val camp: Camp, val matchId: String, val deuxZeroId: String) : ResultatAction
    data object OnRetry : ResultatAction
    data object OnValiderClick : ResultatAction
    data object OnConfirmerValidation : ResultatAction
    data object OnDismissConfirmerValidation : ResultatAction
    data object OnContesterClick : ResultatAction
    data object OnAccepterCorrectionClick : ResultatAction
    data object OnConfirmerAcceptation : ResultatAction
    data object OnDismissConfirmerAcceptation : ResultatAction
    data object OnRefuserCorrectionClick : ResultatAction
    data class OnConfirmerRefus(val commentaire: String) : ResultatAction
    data object OnDismissRefuser : ResultatAction
    data object OnMessageShown : ResultatAction
}
