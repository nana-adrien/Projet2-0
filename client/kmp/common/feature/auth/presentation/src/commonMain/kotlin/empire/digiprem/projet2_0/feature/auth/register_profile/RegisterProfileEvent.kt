package empire.digiprem.projet2_0.feature.auth.register_profile

sealed interface RegisterProfileEvent {
    data object OnRegisterSuccess : RegisterProfileEvent
}
