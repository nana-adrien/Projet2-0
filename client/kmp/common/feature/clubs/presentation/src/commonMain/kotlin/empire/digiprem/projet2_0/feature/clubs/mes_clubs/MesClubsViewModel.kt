package empire.digiprem.projet2_0.feature.clubs.mes_clubs

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.clubs.extension.toUiText
import empire.digiprem.projet2_0.feature.clubs.usecase.ListMyClubsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E22 — Mes clubs (CU5, CU6, CU22). */
class MesClubsViewModel(
    private val listMyClubsUseCase: ListMyClubsUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(MesClubsState())
    val state = _state.asStateFlow()

    private val _events = Channel<MesClubsEvent>()
    val events = _events.receiveAsFlow()

    private var hasLoadedInitialData = false

    fun onAction(action: MesClubsAction) {
        when (action) {
            MesClubsAction.OnLoad -> load()
            is MesClubsAction.OnClubClick -> viewModelScope.launch {
                _events.send(MesClubsEvent.OnNavigateToGestion(action.clubId, action.role))
            }
            MesClubsAction.OnJoinClick -> viewModelScope.launch { _events.send(MesClubsEvent.OnNavigateToJoin) }
            MesClubsAction.OnCreateClick -> viewModelScope.launch { _events.send(MesClubsEvent.OnNavigateToCreate) }
        }
    }

    private fun load() {
        if (hasLoadedInitialData) return
        hasLoadedInitialData = true
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            listMyClubsUseCase()
                .onSuccess { memberships -> _state.update { it.copy(isLoading = false, memberships = memberships.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }
}
