package empire.digiprem.projet2_0.feature.matchendirect.marqueur

sealed interface MarqueurEvent {
    data object OnMatchTermine : MarqueurEvent
}
