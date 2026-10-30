package empire.digiprem.projet2_0.feature.clubs.mes_clubs

import empire.digiprem.projet2_0.feature.clubs.model.MembershipRole

sealed interface MesClubsAction {
    data object OnLoad                                                              : MesClubsAction
    data class OnClubClick(val clubId: String, val role: MembershipRole)           : MesClubsAction
    data object OnJoinClick                                                         : MesClubsAction
    data object OnCreateClick                                                       : MesClubsAction
}
