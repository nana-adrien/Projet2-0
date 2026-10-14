package empire.digiprem.projet2_0.feature.clubs.gestion_club

import empire.digiprem.projet2_0.feature.clubs.model.MembershipRole

sealed interface GestionClubAction {
    data class OnInit(val clubId: String, val initialRole: MembershipRole) : GestionClubAction
    data object OnRetry : GestionClubAction
    data class OnRespondToRequest(val targetUserId: String, val accept: Boolean) : GestionClubAction
    data class OnRemoveMember(val targetUserId: String) : GestionClubAction
    data class OnNameCoAdmin(val targetUserId: String) : GestionClubAction
    data class OnTransferAdmin(val targetUserId: String) : GestionClubAction
    data object OnRegenerateCodeClick : GestionClubAction
    data class OnToggleAcceptationAutomatique(val value: Boolean) : GestionClubAction
    data class OnToggleDelegationCapitaines(val value: Boolean) : GestionClubAction
    data object OnLeaveClubClick : GestionClubAction
    data object OnMessageShown : GestionClubAction
}
