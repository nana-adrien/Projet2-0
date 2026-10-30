package empire.digiprem.projet2_0.feature.clubs.join_club

sealed interface JoinClubEvent {
    data object OnJoined : JoinClubEvent
}
