package empire.digiprem.projet2_0.feature.dashboard.accueil

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.domain.util.todayIso
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.calendrier.extension.toUiText as toCalendrierUiText
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.calendrier.usecase.ListConvocationsUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.ListMatchsUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.RepondreConvocationUseCase
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListMesDeuxZeroUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val STATUT_PROGRAMME = "PROGRAMME"

/** E05 — Accueil (simplifié, voir AccueilState). */
class AccueilViewModel(
    private val listMesDeuxZeroUseCase: ListMesDeuxZeroUseCase,
    private val listMatchsUseCase: ListMatchsUseCase,
    private val listConvocationsUseCase: ListConvocationsUseCase,
    private val repondreConvocationUseCase: RepondreConvocationUseCase,
    private val authSessionStorage: AuthSessionStorage,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(AccueilState())
    val state = _state.asStateFlow()

    private val _events = Channel<AccueilEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: AccueilAction) {
        when (action) {
            AccueilAction.OnLoad -> init()
            AccueilAction.OnRetry -> loadMesDeuxZero()
            AccueilAction.OnChangerDeuxZeroClick -> _state.update { it.copy(showChangerDeuxZero = true) }
            AccueilAction.OnDismissChangerDeuxZero -> _state.update { it.copy(showChangerDeuxZero = false) }
            is AccueilAction.OnSelectDeuxZero -> selectDeuxZero(action.deuxZero)
            is AccueilAction.OnRepondre -> repondre(action.reponse)
            is AccueilAction.OnMatchClick -> viewModelScope.launch {
                _state.value.deuxZeroActuel?.id?.let { deuxZeroId ->
                    _events.send(AccueilEvent.OnNavigateToFicheMatch(action.match, deuxZeroId))
                }
            }
            AccueilAction.OnMessageShown -> _state.update { it.copy(message = null) }
        }
    }

    private fun init() {
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch { _state.update { it.copy(currentUserId = authSessionStorage.getData()?.userId) } }
        loadMesDeuxZero()
    }

    private fun loadMesDeuxZero() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            listMesDeuxZeroUseCase()
                .onSuccess { mesDeuxZero ->
                    val liste = mesDeuxZero.orEmpty()
                    val actuel = liste.firstOrNull()
                    _state.update { it.copy(mesDeuxZero = liste, deuxZeroActuel = actuel) }
                    if (actuel == null) {
                        _state.update { it.copy(isLoading = false) }
                    } else {
                        loadProchainMatch(actuel)
                    }
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toDeuxZeroUiText()) } }
        }
    }

    private fun selectDeuxZero(deuxZero: DeuxZero) {
        _state.update { it.copy(deuxZeroActuel = deuxZero, showChangerDeuxZero = false, prochainMatch = null, convocations = emptyList()) }
        loadProchainMatch(deuxZero)
    }

    private fun loadProchainMatch(deuxZero: DeuxZero) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            listMatchsUseCase(deuxZero.id)
                .onSuccess { matchs ->
                    val aujourdHui = todayIso()
                    val prochain = matchs.orEmpty()
                        .filter { it.statut == STATUT_PROGRAMME && it.date >= aujourdHui }
                        .minByOrNull { it.date + it.heure }
                    _state.update { it.copy(isLoading = false, prochainMatch = prochain) }
                    if (prochain != null) loadConvocations(prochain.id) else _state.update { it.copy(convocations = emptyList()) }
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toCalendrierUiText()) } }
        }
    }

    private fun loadConvocations(matchId: String) {
        viewModelScope.launch {
            listConvocationsUseCase(matchId)
                .onSuccess { convocations -> _state.update { it.copy(convocations = convocations.orEmpty()) } }
                .onFailure { /* pas de convocations générées : état silencieux, pas d'erreur bloquante */ }
        }
    }

    private fun repondre(reponse: ReponseConvocation) {
        val convocationId = _state.value.maConvocation?.id ?: return
        viewModelScope.launch {
            repondreConvocationUseCase(convocationId, reponse)
                .onSuccess { _state.value.prochainMatch?.let { loadConvocations(it.id) } }
                .onFailure { error -> _state.update { it.copy(message = error.toCalendrierUiText()) } }
        }
    }
}
