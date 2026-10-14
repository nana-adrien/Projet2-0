package empire.digiprem.projet2_0.feature.deuxzero.composer_effectif

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.clubs.extension.toUiText as toClubUiText
import empire.digiprem.projet2_0.feature.clubs.usecase.ListMembersUseCase
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.usecase.AddEffectifUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListEffectifUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.NommerCapitaineUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** CU10 — Composer l'effectif engagé d'un camp. */
class ComposerEffectifViewModel(
    private val listMembersUseCase: ListMembersUseCase,
    private val listEffectifUseCase: ListEffectifUseCase,
    private val addEffectifUseCase: AddEffectifUseCase,
    private val nommerCapitaineUseCase: NommerCapitaineUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(ComposerEffectifState())
    val state = _state.asStateFlow()

    private val _events = Channel<ComposerEffectifEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: ComposerEffectifAction) {
        when (action) {
            is ComposerEffectifAction.OnInit -> init(action.camp, action.clubId)
            ComposerEffectifAction.OnRetry -> load()
            is ComposerEffectifAction.OnAddMember -> addMember(action.utilisateurId)
            is ComposerEffectifAction.OnNameCaptain -> nameCaptain(action.utilisateurId, action.viceCapitaine)
            ComposerEffectifAction.OnMessageShown -> _state.update { it.copy(message = null) }
            ComposerEffectifAction.OnDoneClick -> viewModelScope.launch { _events.send(ComposerEffectifEvent.OnDone) }
            ComposerEffectifAction.OnViewCalendrierClick -> viewModelScope.launch {
                _state.value.camp?.deuxZeroId?.let { _events.send(ComposerEffectifEvent.OnViewCalendrier(it)) }
            }
        }
    }

    private fun init(camp: Camp, clubId: String) {
        _state.update { it.copy(camp = camp, clubId = clubId) }
        if (hasInitialized) return
        hasInitialized = true
        load()
    }

    private fun load() {
        val clubId = _state.value.clubId
        val campId = _state.value.camp?.id ?: return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            listMembersUseCase(clubId)
                .onSuccess { members -> _state.update { it.copy(clubMembers = members.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toClubUiText()) } }

            listEffectifUseCase(campId)
                .onSuccess { effectif -> _state.update { it.copy(isLoading = false, effectif = effectif.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun addMember(utilisateurId: String) {
        val campId = _state.value.camp?.id ?: return
        viewModelScope.launch {
            addEffectifUseCase(campId, utilisateurId, numeroMaillot = null)
                .onSuccess { load() }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }

    private fun nameCaptain(utilisateurId: String, viceCapitaine: Boolean) {
        val campId = _state.value.camp?.id ?: return
        viewModelScope.launch {
            nommerCapitaineUseCase(campId, utilisateurId, viceCapitaine)
                .onSuccess { camp ->
                    _state.update {
                        it.copy(
                            camp = camp ?: it.camp,
                            message = UiText.DynamicString(if (viceCapitaine) "Vice-capitaine nommé." else "Capitaine nommé."),
                        )
                    }
                }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }
}
