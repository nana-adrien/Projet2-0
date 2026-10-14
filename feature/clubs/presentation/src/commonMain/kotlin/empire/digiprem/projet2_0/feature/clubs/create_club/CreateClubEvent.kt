package empire.digiprem.projet2_0.feature.clubs.create_club

sealed interface CreateClubEvent {
    data class OnCreated(val clubId: String) : CreateClubEvent
}
