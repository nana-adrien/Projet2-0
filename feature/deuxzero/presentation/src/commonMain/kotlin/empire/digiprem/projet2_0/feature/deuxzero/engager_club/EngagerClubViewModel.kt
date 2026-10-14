package empire.digiprem.projet2_0.feature.deuxzero.engager_club

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText
import empire.digiprem.projet2_0.feature.deuxzero.usecase.EngageClubUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** CU9 — L'administrateur d'un club saisit le code d'engagement transmis par l'organisateur. */
class EngagerClubViewModel(
    private val engageClubUseCase: EngageClubUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(EngagerClubState())
    val state = _state.asStateFlow()

    private val _events = Channel<EngagerClubEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: EngagerClubAction) {
        when (action) {
            is EngagerClubAction.OnCodeChange -> _state.update { it.copy(code = action.value, errorMessage = null) }
            EngagerClubAction.OnSubmit -> submit()
        }
    }

    private fun submit() {
        val code = _state.value.code.trim()
        if (code.isBlank()) return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            engageClubUseCase(code)
                .onSuccess { camp ->
                    _state.update { it.copy(isLoading = false) }
                    camp?.let { _events.send(EngagerClubEvent.OnEngaged(it)) }
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }
}
