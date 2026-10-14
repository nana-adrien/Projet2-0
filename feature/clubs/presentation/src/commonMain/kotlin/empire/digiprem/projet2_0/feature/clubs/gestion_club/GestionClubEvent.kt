package empire.digiprem.projet2_0.feature.clubs.gestion_club

sealed interface GestionClubEvent {
    data object OnLeftClub : GestionClubEvent
}
