package empire.digiprem.projet2_0.feature.calendrier.calendrier

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.calendrier.extension.toUiText
import empire.digiprem.projet2_0.feature.calendrier.usecase.AjouterMatchExceptionnelUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.GenererCalendrierUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.ListMatchsUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListMesDeuxZeroUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E07 — Calendrier des matchs d'un 2-0 (CU14). */
class CalendrierViewModel(
    private val listMatchsUseCase: ListMatchsUseCase,
    private val genererCalendrierUseCase: GenererCalendrierUseCase,
    private val ajouterMatchExceptionnelUseCase: AjouterMatchExceptionnelUseCase,
    private val listMesDeuxZeroUseCase: ListMesDeuxZeroUseCase,
    private val authSessionStorage: AuthSessionStorage,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(CalendrierState())
    val state = _state.asStateFlow()

    private val _events = Channel<CalendrierEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: CalendrierAction) {
        when (action) {
            is CalendrierAction.OnInit -> init(action.deuxZeroId)
            CalendrierAction.OnRetry -> load()
            is CalendrierAction.OnFiltreChange -> _state.update { it.copy(filtre = action.filtre) }
            CalendrierAction.OnGenererCalendrierClick -> genererCalendrier()
            CalendrierAction.OnAjouterMatchClick -> _state.update { it.copy(showAjouterMatchDialog = true) }
            CalendrierAction.OnDismissAjouterMatchDialog -> _state.update { it.copy(showAjouterMatchDialog = false) }
            is CalendrierAction.OnAjouterMatchConfirm -> ajouterMatch(action.date, action.heure, action.lieu)
            is CalendrierAction.OnMatchClick -> viewModelScope.launch {
                _events.send(CalendrierEvent.OnNavigateToFicheMatch(action.match))
            }
            CalendrierAction.OnParametresClick -> viewModelScope.launch {
                _events.send(CalendrierEvent.OnNavigateToParametres(_state.value.deuxZeroId))
            }
        }
    }

    private fun init(deuxZeroId: String) {
        _state.update { it.copy(deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch { _state.update { it.copy(currentUserId = authSessionStorage.getData()?.userId) } }
        load()
    }

    private fun load() {
        val deuxZeroId = _state.value.deuxZeroId
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            listMatchsUseCase(deuxZeroId)
                .onSuccess { matchs -> _state.update { it.copy(isLoading = false, matchs = matchs.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }

            val currentUserId = _state.value.currentUserId
            listMesDeuxZeroUseCase()
                .onSuccess { deuxZeros ->
                    val estOrganisateur = deuxZeros.orEmpty().any { it.id == deuxZeroId && it.organisateurId == currentUserId }
                    _state.update { it.copy(peutModifierParametres = estOrganisateur) }
                }
                .onFailure { /* non bloquant : le bouton "Paramètres" reste simplement masqué */ }
        }
    }

    private fun genererCalendrier() {
        val deuxZeroId = _state.value.deuxZeroId
        _state.update { it.copy(isGenerating = true, errorMessage = null) }
        viewModelScope.launch {
            genererCalendrierUseCase(deuxZeroId)
                .onSuccess { _state.update { it.copy(isGenerating = false) }; load() }
                .onFailure { error -> _state.update { it.copy(isGenerating = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun ajouterMatch(date: String, heure: String, lieu: String?) {
        val deuxZeroId = _state.value.deuxZeroId
        viewModelScope.launch {
            ajouterMatchExceptionnelUseCase(deuxZeroId, date, heure, lieu)
                .onSuccess { _state.update { it.copy(showAjouterMatchDialog = false) }; load() }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toUiText()) } }
        }
    }
}
