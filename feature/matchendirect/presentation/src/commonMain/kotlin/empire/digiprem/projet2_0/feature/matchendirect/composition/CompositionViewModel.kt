package empire.digiprem.projet2_0.feature.matchendirect.composition

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.calendrier.extension.toUiText as toCalendrierUiText
import empire.digiprem.projet2_0.feature.calendrier.usecase.ListConvocationsUseCase
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListEffectifUseCase
import empire.digiprem.projet2_0.feature.matchendirect.extension.toUiText
import empire.digiprem.projet2_0.feature.matchendirect.usecase.DesignerCompositionUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.DesignerMarqueurUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.GetMatchEnDirectUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.ListCompositionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E09 — Composition et marqueur (CU16). */
class CompositionViewModel(
    private val listEffectifUseCase: ListEffectifUseCase,
    private val listConvocationsUseCase: ListConvocationsUseCase,
    private val listCompositionUseCase: ListCompositionUseCase,
    private val designerCompositionUseCase: DesignerCompositionUseCase,
    private val designerMarqueurUseCase: DesignerMarqueurUseCase,
    private val getMatchEnDirectUseCase: GetMatchEnDirectUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(CompositionState())
    val state = _state.asStateFlow()

    private val _events = Channel<CompositionEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: CompositionAction) {
        when (action) {
            is CompositionAction.OnInit -> init(action.camp, action.matchId)
            CompositionAction.OnRetry -> load()
            is CompositionAction.OnToggleTitulaire -> toggle(action.utilisateurId, titulaire = true)
            is CompositionAction.OnToggleRemplacant -> toggle(action.utilisateurId, titulaire = false)
            CompositionAction.OnChangerMarqueurClick -> _state.update { it.copy(showMarqueurPicker = true) }
            CompositionAction.OnChangerSuppleantClick -> _state.update { it.copy(showSuppleantPicker = true) }
            CompositionAction.OnDismissPicker -> _state.update { it.copy(showMarqueurPicker = false, showSuppleantPicker = false) }
            is CompositionAction.OnSelectMarqueur -> designerMarqueur(action.utilisateurId, suppleant = false)
            is CompositionAction.OnSelectSuppleant -> designerMarqueur(action.utilisateurId, suppleant = true)
            CompositionAction.OnMessageShown -> _state.update { it.copy(message = null) }
            CompositionAction.OnDoneClick -> viewModelScope.launch { _events.send(CompositionEvent.OnDone) }
        }
    }

    private fun init(camp: Camp, matchId: String) {
        _state.update { it.copy(camp = camp, matchId = matchId) }
        if (hasInitialized) return
        hasInitialized = true
        load()
    }

    private fun load() {
        val campId = _state.value.camp?.id ?: return
        val matchId = _state.value.matchId
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            listEffectifUseCase(campId)
                .onSuccess { effectif -> _state.update { it.copy(effectif = effectif.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toDeuxZeroUiText()) } }

            listConvocationsUseCase(matchId)
                .onSuccess { convocations -> _state.update { it.copy(convocations = convocations.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toCalendrierUiText()) } }

            listCompositionUseCase(matchId)
                .onSuccess { composition -> _state.update { it.copy(composition = composition.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toUiText()) } }

            getMatchEnDirectUseCase(matchId)
                .onSuccess { matchEnDirect -> _state.update { it.copy(isLoading = false, match = matchEnDirect?.match) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun toggle(utilisateurId: String, titulaire: Boolean) {
        val matchId = _state.value.matchId
        viewModelScope.launch {
            designerCompositionUseCase(matchId, utilisateurId, titulaire = titulaire, present = true)
                .onSuccess { load() }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }

    private fun designerMarqueur(utilisateurId: String, suppleant: Boolean) {
        val matchId = _state.value.matchId
        viewModelScope.launch {
            designerMarqueurUseCase(matchId, utilisateurId, suppleant)
                .onSuccess {
                    _state.update {
                        it.copy(
                            showMarqueurPicker = false, showSuppleantPicker = false,
                            message = UiText.DynamicString(if (suppleant) "Suppléant désigné." else "Marqueur désigné."),
                        )
                    }
                    load()
                }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }
}
