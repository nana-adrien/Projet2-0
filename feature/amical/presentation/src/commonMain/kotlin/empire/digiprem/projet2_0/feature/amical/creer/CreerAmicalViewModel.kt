package empire.digiprem.projet2_0.feature.amical.creer

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.amical.extension.toUiText
import empire.digiprem.projet2_0.feature.amical.model.CreerMatchAmicalInput
import empire.digiprem.projet2_0.feature.amical.usecase.CreerMatchAmicalUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E26 — Nouveau match amical (CU21). */
class CreerAmicalViewModel(
    private val creerMatchAmicalUseCase: CreerMatchAmicalUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(CreerAmicalState())
    val state = _state.asStateFlow()

    private val _events = Channel<CreerAmicalEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CreerAmicalAction) {
        when (action) {
            is CreerAmicalAction.OnInit -> _state.update { it.copy(clubId = action.clubId) }
            is CreerAmicalAction.OnNomEquipeChange -> _state.update { it.copy(nomEquipeInvitee = action.value) }
            is CreerAmicalAction.OnDateChange -> _state.update { it.copy(date = action.value) }
            is CreerAmicalAction.OnHeureChange -> _state.update { it.copy(heure = action.value) }
            is CreerAmicalAction.OnDureeMiTempsChange -> _state.update { it.copy(dureeMiTemps = action.value) }
            CreerAmicalAction.OnCreerEtConvoquerClick -> creer()
        }
    }

    private fun creer() {
        val current = _state.value
        if (!current.peutCreer) return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val input = CreerMatchAmicalInput(
                clubId = current.clubId,
                nomEquipeInvitee = current.nomEquipeInvitee,
                date = current.date,
                heure = current.heure,
                dureeMiTempsMinutes = current.dureeMiTemps.minutes,
            )
            creerMatchAmicalUseCase(input)
                .onSuccess { match ->
                    _state.update { it.copy(isLoading = false) }
                    match?.let { _events.send(CreerAmicalEvent.OnCree(it.id)) }
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }
}
