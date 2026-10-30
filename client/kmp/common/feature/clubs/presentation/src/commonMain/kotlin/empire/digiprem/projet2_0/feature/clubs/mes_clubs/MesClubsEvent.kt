package empire.digiprem.projet2_0.feature.clubs.mes_clubs

import empire.digiprem.projet2_0.feature.clubs.model.MembershipRole

sealed interface MesClubsEvent {
    data class OnNavigateToGestion(val clubId: String, val role: MembershipRole) : MesClubsEvent
    data object OnNavigateToJoin                                                  : MesClubsEvent
    data object OnNavigateToCreate                                                : MesClubsEvent
}
