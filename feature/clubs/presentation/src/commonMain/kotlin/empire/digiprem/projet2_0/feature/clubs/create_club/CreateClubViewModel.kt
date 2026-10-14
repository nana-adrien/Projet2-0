package empire.digiprem.projet2_0.feature.clubs.create_club

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.clubs.extension.toUiText
import empire.digiprem.projet2_0.feature.clubs.usecase.CreateClubUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** CU5 — Créer un club. */
class CreateClubViewModel(
    private val createClubUseCase: CreateClubUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(CreateClubState())
    val state = _state.asStateFlow()

    private val _events = Channel<CreateClubEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CreateClubAction) {
        when (action) {
            is CreateClubAction.OnNomChange -> _state.update { it.copy(nom = action.value) }
            is CreateClubAction.OnQuartierChange -> _state.update { it.copy(quartier = action.value) }
            is CreateClubAction.OnDescriptionChange -> _state.update { it.copy(description = action.value) }
            CreateClubAction.OnSubmit -> submit()
        }
    }

    private fun submit() {
        val current = _state.value
        if (current.nom.isBlank()) return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            createClubUseCase(
                nom = current.nom.trim(),
                logo = null,
                couleurs = null,
                quartier = current.quartier.trim().takeIf { it.isNotBlank() },
                description = current.description.trim().takeIf { it.isNotBlank() },
            )
                .onSuccess { club ->
                    _state.update { it.copy(isLoading = false) }
                    club?.let { _events.send(CreateClubEvent.OnCreated(it.id)) }
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }
}
