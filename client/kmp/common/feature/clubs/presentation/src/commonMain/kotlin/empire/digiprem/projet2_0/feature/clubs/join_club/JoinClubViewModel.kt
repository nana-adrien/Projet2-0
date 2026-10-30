package empire.digiprem.projet2_0.feature.clubs.join_club

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.clubs.extension.toUiText
import empire.digiprem.projet2_0.feature.clubs.usecase.JoinClubUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** CU6 — Rejoindre un club. */
class JoinClubViewModel(
    private val joinClubUseCase: JoinClubUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(JoinClubState())
    val state = _state.asStateFlow()

    private val _events = Channel<JoinClubEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: JoinClubAction) {
        when (action) {
            is JoinClubAction.OnCodeChange -> _state.update { it.copy(code = action.value, errorMessage = null) }
            JoinClubAction.OnSubmit -> submit()
        }
    }

    private fun submit() {
        val code = _state.value.code.trim()
        if (code.isBlank()) return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            joinClubUseCase(code)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(JoinClubEvent.OnJoined)
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }
}
