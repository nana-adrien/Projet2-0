package empire.digiprem.projet2_0.feature.settings.settings

sealed interface SettingsEvent {
    data object OnLogout : SettingsEvent
}
