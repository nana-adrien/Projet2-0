package empire.digiprem.projet2_0.feature.settings.settings

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.settings.model.PDZSettings
import empire.digiprem.projet2_0.feature.settings.usecase.GetSettingsUseCase
import empire.digiprem.projet2_0.feature.settings.usecase.UpdateSettingsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getSettingsUseCase    : GetSettingsUseCase,
    private val updateSettingsUseCase : UpdateSettingsUseCase,
    private val authSessionStorage    : AuthSessionStorage,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.stateIn(
        scope            = viewModelScope,
        started          = SharingStarted.WhileSubscribed(5_000L),
        initialValue     = SettingsState(),
    )

    private val _events = Channel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    init {
        getSettingsUseCase()
            .onEach { settings -> _state.update { it.copy(settings = settings) } }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnDarkThemeToggle     -> updateSettings { it.copy(isDarkTheme = action.enabled) }
            is SettingsAction.OnLanguageChange      -> updateSettings { it.copy(language = action.language) }
            is SettingsAction.OnNotificationsToggle -> updateSettings { it.copy(notificationsEnabled = action.enabled) }
            SettingsAction.OnLogout                 -> logout()
        }
    }

    private fun updateSettings(transform: (PDZSettings) -> PDZSettings) {
        viewModelScope.launch {
            updateSettingsUseCase(transform(_state.value.settings))
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authSessionStorage.clear()
            _events.send(SettingsEvent.OnLogout)
        }
    }
}
