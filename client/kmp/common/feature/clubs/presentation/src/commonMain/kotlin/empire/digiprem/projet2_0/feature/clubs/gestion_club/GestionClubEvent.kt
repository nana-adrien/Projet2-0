package empire.digiprem.projet2_0.feature.clubs.gestion_club

sealed interface GestionClubEvent {
    data object OnLeftClub : GestionClubEvent
    data class OnNavigateToCreateDeuxZero(val clubId: String) : GestionClubEvent
    data class OnNavigateToEngagerClub(val clubId: String) : GestionClubEvent
    data class OnNavigateToOrganiserAmical(val clubId: String) : GestionClubEvent
}
