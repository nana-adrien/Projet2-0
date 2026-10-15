package empire.digiprem.projet2_0.feature.settings.settings

import empire.digiprem.projet2_0.feature.settings.model.PDZSettings

data class SettingsState(
    val settings  : PDZSettings = PDZSettings(),
    val isLoading : Boolean     = false,
)
