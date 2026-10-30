package empire.digiprem.projet2_0.feature.social.signaler

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.social.extension.toUiText
import empire.digiprem.projet2_0.feature.social.usecase.SignalerUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Composant réutilisable "Signaler" (CU27). */
class SignalerViewModel(
    private val signalerUseCase: SignalerUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(SignalerState())
    val state = _state.asStateFlow()

    private val _events = Channel<SignalerEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SignalerAction) {
        when (action) {
            is SignalerAction.OnInit -> _state.update { it.copy(contenuType = action.contenuType, contenuId = action.contenuId) }
            is SignalerAction.OnMotifChange -> _state.update { it.copy(motif = action.value) }
            SignalerAction.OnEnvoyerClick -> envoyer()
        }
    }

    private fun envoyer() {
        val current = _state.value
        val contenuType = current.contenuType ?: return
        if (!current.peutEnvoyer) return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            signalerUseCase(contenuType, current.contenuId, current.motif)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(SignalerEvent.OnEnvoye)
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }
}
