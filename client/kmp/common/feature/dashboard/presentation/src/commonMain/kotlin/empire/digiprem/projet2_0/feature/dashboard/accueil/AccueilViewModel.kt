package empire.digiprem.projet2_0.feature.dashboard.accueil

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.domain.util.todayIso
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.calendrier.extension.toUiText as toCalendrierUiText
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.calendrier.usecase.GetPresencesUseCase
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
private const val STATUT_OFFICIEL = "OFFICIEL"

/** E05 — Accueil (simplifié, voir AccueilState). */
class AccueilViewModel(
    private val listMesDeuxZeroUseCase: ListMesDeuxZeroUseCase,
    private val listMatchsUseCase: ListMatchsUseCase,
    private val listConvocationsUseCase: ListConvocationsUseCase,
    private val repondreConvocationUseCase: RepondreConvocationUseCase,
    private val getPresencesUseCase: GetPresencesUseCase,
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
            AccueilAction.OnNotificationsClick -> viewModelScope.launch { _events.send(AccueilEvent.OnNavigateToNotifications) }
            AccueilAction.OnNavigateToClubsClick -> viewModelScope.launch { _events.send(AccueilEvent.OnNavigateToClubs) }
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
        _state.update {
            it.copy(
                deuxZeroActuel = deuxZero,
                showChangerDeuxZero = false,
                prochainMatch = null,
                dernierResultat = null,
                convocations = emptyList(),
                presences = null,
            )
        }
        loadProchainMatch(deuxZero)
    }

    private fun loadProchainMatch(deuxZero: DeuxZero) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            listMatchsUseCase(deuxZero.id)
                .onSuccess { matchs ->
                    val liste = matchs.orEmpty()
                    val aujourdHui = todayIso()
                    val prochain = liste
                        .filter { it.statut == STATUT_PROGRAMME && it.date >= aujourdHui }
                        .minByOrNull { it.date + it.heure }
                    val dernierResultat = liste
                        .filter { it.statut == STATUT_OFFICIEL && it.scoreCampA != null && it.scoreCampB != null }
                        .maxByOrNull { it.date + it.heure }
                    _state.update { it.copy(isLoading = false, prochainMatch = prochain, dernierResultat = dernierResultat) }
                    if (prochain != null) loadConvocations(prochain.id) else _state.update { it.copy(convocations = emptyList(), presences = null) }
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toCalendrierUiText()) } }
        }
    }

    private fun loadConvocations(matchId: String) {
        viewModelScope.launch {
            listConvocationsUseCase(matchId)
                .onSuccess { convocations -> _state.update { it.copy(convocations = convocations.orEmpty()) } }
                .onFailure { /* pas de convocations générées : état silencieux, pas d'erreur bloquante */ }

            getPresencesUseCase(matchId)
                .onSuccess { presences -> _state.update { it.copy(presences = presences) } }
                .onFailure { /* décompte par camp non bloquant : l'écran retombe sur le total */ }
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
