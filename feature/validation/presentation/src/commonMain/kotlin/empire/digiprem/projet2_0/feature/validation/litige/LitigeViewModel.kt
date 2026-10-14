package empire.digiprem.projet2_0.feature.validation.litige

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import empire.digiprem.projet2_0.feature.validation.extension.toUiText
import empire.digiprem.projet2_0.feature.validation.usecase.GetResultatUseCase
import empire.digiprem.projet2_0.feature.validation.usecase.TrancherLitigeUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E16 — Arbitrer un litige (CU20). */
class LitigeViewModel(
    private val listCampsUseCase: ListCampsUseCase,
    private val getResultatUseCase: GetResultatUseCase,
    private val trancherLitigeUseCase: TrancherLitigeUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(LitigeState())
    val state = _state.asStateFlow()

    private val _events = Channel<LitigeEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: LitigeAction) {
        when (action) {
            is LitigeAction.OnInit -> init(action.matchId, action.deuxZeroId)
            LitigeAction.OnRetry -> load()
            is LitigeAction.OnDecisionClick -> _state.update { it.copy(decisionChoisie = action.decision, showMotifDialog = true) }
            LitigeAction.OnDismissMotifDialog -> _state.update { it.copy(showMotifDialog = false, decisionChoisie = null) }
            is LitigeAction.OnConfirmerDecision -> trancher(action.motif)
            LitigeAction.OnMessageShown -> _state.update { it.copy(message = null) }
        }
    }

    private fun init(matchId: String, deuxZeroId: String) {
        _state.update { it.copy(matchId = matchId, deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        load()
    }

    private fun load() {
        val matchId = _state.value.matchId
        val deuxZeroId = _state.value.deuxZeroId
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            listCampsUseCase(deuxZeroId)
                .onSuccess { camps -> _state.update { it.copy(campA = camps?.getOrNull(0), campB = camps?.getOrNull(1)) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toDeuxZeroUiText()) } }

            getResultatUseCase(matchId)
                .onSuccess { resultat -> _state.update { it.copy(isLoading = false, resultat = resultat) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun trancher(motif: String) {
        val matchId = _state.value.matchId
        val decision = _state.value.decisionChoisie ?: return
        _state.update { it.copy(isActionLoading = true, showMotifDialog = false) }
        viewModelScope.launch {
            trancherLitigeUseCase(matchId, decision, motif)
                .onSuccess {
                    _state.update { s -> s.copy(isActionLoading = false, resultat = it ?: s.resultat, message = UiText.DynamicString("Décision enregistrée.")) }
                    _events.send(LitigeEvent.OnTranche)
                }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }
}
