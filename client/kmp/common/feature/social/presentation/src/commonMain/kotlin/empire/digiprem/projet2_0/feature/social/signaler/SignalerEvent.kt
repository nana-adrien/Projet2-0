package empire.digiprem.projet2_0.feature.social.signaler

sealed interface SignalerEvent {
    data object OnEnvoye : SignalerEvent
}
