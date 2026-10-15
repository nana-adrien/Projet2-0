package empire.digiprem.projet2_0.feature.notifications.preferences

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListMesDeuxZeroUseCase
import empire.digiprem.projet2_0.feature.notifications.extension.toUiText
import empire.digiprem.projet2_0.feature.notifications.model.NiveauNotification
import empire.digiprem.projet2_0.feature.notifications.usecase.GetPreferenceNotificationUseCase
import empire.digiprem.projet2_0.feature.notifications.usecase.SetPreferenceNotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E27 — Préférences de notification (CU26). */
class PreferenceViewModel(
    private val listMesDeuxZeroUseCase: ListMesDeuxZeroUseCase,
    private val getPreferenceNotificationUseCase: GetPreferenceNotificationUseCase,
    private val setPreferenceNotificationUseCase: SetPreferenceNotificationUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(PreferenceState())
    val state = _state.asStateFlow()

    private var hasInitialized = false

    fun onAction(action: PreferenceAction) {
        when (action) {
            PreferenceAction.OnInit -> init()
            PreferenceAction.OnRetry -> chargerPreference()
            is PreferenceAction.OnDeuxZeroChange -> {
                _state.update { it.copy(deuxZeroSelectionneId = action.deuxZeroId) }
                chargerPreference()
            }
            is PreferenceAction.OnNiveauChange -> _state.update { it.copy(niveau = action.niveau) }
            is PreferenceAction.OnNeRienDerangerChange -> _state.update { it.copy(neRienDerangerNocturne = action.value) }
            PreferenceAction.OnEnregistrerClick -> enregistrer()
            PreferenceAction.OnMessageShown -> _state.update { it.copy(message = null) }
        }
    }

    private fun init() {
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch {
            listMesDeuxZeroUseCase()
                .onSuccess { deuxZeros ->
                    _state.update { it.copy(deuxZeros = deuxZeros.orEmpty(), deuxZeroSelectionneId = deuxZeros?.firstOrNull()?.id.orEmpty()) }
                    chargerPreference()
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toDeuxZeroUiText()) } }
        }
    }

    private fun chargerPreference() {
        val deuxZeroId = _state.value.deuxZeroSelectionneId
        if (deuxZeroId.isBlank()) return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getPreferenceNotificationUseCase(deuxZeroId)
                .onSuccess { preference ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            niveau = preference?.niveau ?: NiveauNotification.TOUT,
                            neRienDerangerNocturne = preference?.neRienDerangerNocturne ?: false,
                        )
                    }
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun enregistrer() {
        val deuxZeroId = _state.value.deuxZeroSelectionneId
        val niveau = _state.value.niveau
        val neRienDeranger = _state.value.neRienDerangerNocturne
        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            setPreferenceNotificationUseCase(deuxZeroId, niveau, neRienDeranger)
                .onSuccess { _state.update { it.copy(isActionLoading = false, message = UiText.DynamicString("Préférences enregistrées.")) } }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }
}
