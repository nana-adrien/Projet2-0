package empire.digiprem.projet2_0.feature.matchendirect.suivre

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListEffectifUseCase
import empire.digiprem.projet2_0.feature.matchendirect.extension.toUiText
import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.usecase.GetMatchEnDirectUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.ListCompositionUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.ObserveMatchEnDirectUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E12 — Suivre le match en direct (CU18), lecture seule via WebSocket. */
class SuivreEnDirectViewModel(
    private val listCampsUseCase: ListCampsUseCase,
    private val listEffectifUseCase: ListEffectifUseCase,
    private val listCompositionUseCase: ListCompositionUseCase,
    private val getMatchEnDirectUseCase: GetMatchEnDirectUseCase,
    private val observeMatchEnDirectUseCase: ObserveMatchEnDirectUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(SuivreEnDirectState())
    val state = _state.asStateFlow()

    private var hasInitialized = false

    fun onAction(action: SuivreEnDirectAction) {
        when (action) {
            is SuivreEnDirectAction.OnInit -> init(action.matchId, action.deuxZeroId)
            SuivreEnDirectAction.OnRetry -> load()
            is SuivreEnDirectAction.OnTabChange -> _state.update { it.copy(tab = action.tab) }
        }
    }

    private fun init(matchId: String, deuxZeroId: String) {
        _state.update { it.copy(matchId = matchId, deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        load()
        observerMatchEnDirect(matchId)
    }

    private fun load() {
        val deuxZeroId = _state.value.deuxZeroId
        val matchId = _state.value.matchId
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            listCampsUseCase(deuxZeroId)
                .onSuccess { camps ->
                    val campA = camps?.getOrNull(0)
                    val campB = camps?.getOrNull(1)
                    _state.update { it.copy(campA = campA, campB = campB) }
                    campA?.let { chargerEffectif(it, estCampA = true) }
                    campB?.let { chargerEffectif(it, estCampA = false) }
                }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toDeuxZeroUiText()) } }

            listCompositionUseCase(matchId)
                .onSuccess { compositions -> _state.update { it.copy(compositions = compositions.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toUiText()) } }

            getMatchEnDirectUseCase(matchId)
                .onSuccess { matchEnDirect -> appliquer(matchEnDirect) }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }

    private suspend fun chargerEffectif(camp: Camp, estCampA: Boolean) {
        listEffectifUseCase(camp.id)
            .onSuccess { effectif ->
                _state.update { if (estCampA) it.copy(effectifA = effectif.orEmpty()) else it.copy(effectifB = effectif.orEmpty()) }
            }
            .onFailure { error -> _state.update { it.copy(errorMessage = error.toDeuxZeroUiText()) } }
    }

    private fun observerMatchEnDirect(matchId: String) {
        viewModelScope.launch {
            observeMatchEnDirectUseCase(matchId).collect { matchEnDirect ->
                val ancienDernierId = _state.value.chronologie.firstOrNull()?.id
                appliquer(matchEnDirect)
                val nouveauDernierId = _state.value.chronologie.firstOrNull()?.id
                if (nouveauDernierId != null && nouveauDernierId != ancienDernierId) {
                    surligner(nouveauDernierId)
                }
            }
        }
    }

    private fun surligner(evenementId: String) {
        viewModelScope.launch {
            _state.update { it.copy(idEvenementSurligne = evenementId) }
            delay(2_000)
            if (_state.value.idEvenementSurligne == evenementId) {
                _state.update { it.copy(idEvenementSurligne = null) }
            }
        }
    }

    private fun appliquer(matchEnDirect: MatchEnDirect?) {
        _state.update {
            it.copy(
                isLoading = false,
                match = matchEnDirect?.match ?: it.match,
                evenements = matchEnDirect?.evenements ?: it.evenements,
            )
        }
    }
}
