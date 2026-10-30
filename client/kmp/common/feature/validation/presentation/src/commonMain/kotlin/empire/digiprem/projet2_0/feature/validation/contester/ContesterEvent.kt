package empire.digiprem.projet2_0.feature.validation.contester

sealed interface ContesterEvent {
    data object OnEnvoye : ContesterEvent
}
