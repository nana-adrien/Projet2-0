package empire.digiprem.projet2_0.feature.amical.creer

sealed interface CreerAmicalEvent {
    data class OnCree(val matchId: String) : CreerAmicalEvent
}
