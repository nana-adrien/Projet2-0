package empire.digiprem.projet2_0.feature.matchendirect.composition

sealed interface CompositionEvent {
    data object OnDone : CompositionEvent
}
