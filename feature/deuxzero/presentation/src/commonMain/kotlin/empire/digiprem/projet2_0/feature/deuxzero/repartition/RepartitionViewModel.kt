package empire.digiprem.projet2_0.feature.deuxzero.repartition

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.clubs.extension.toUiText as toClubUiText
import empire.digiprem.projet2_0.feature.clubs.usecase.ListMembersUseCase
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.usecase.AddEffectifUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E25 — Répartir les membres entre les deux camps d'un 2-0 interne (CU10). */
class RepartitionViewModel(
    private val listMembersUseCase: ListMembersUseCase,
    private val addEffectifUseCase: AddEffectifUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(RepartitionState())
    val state = _state.asStateFlow()

    private val _events = Channel<RepartitionEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: RepartitionAction) {
        when (action) {
            is RepartitionAction.OnInit -> init(action.campA, action.campB, action.clubId)
            RepartitionAction.OnRetry -> load()
            is RepartitionAction.OnAssign -> _state.update { it.copy(assignments = it.assignments + (action.utilisateurId to action.side)) }
            RepartitionAction.OnTirageEquilibre -> tirageEquilibre()
            RepartitionAction.OnValiderClick -> valider()
        }
    }

    private fun init(campA: Camp, campB: Camp, clubId: String) {
        _state.update { it.copy(campA = campA, campB = campB, clubId = clubId) }
        if (hasInitialized) return
        hasInitialized = true
        load()
    }

    private fun load() {
        val clubId = _state.value.clubId
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            listMembersUseCase(clubId)
                .onSuccess { members -> _state.update { it.copy(isLoading = false, clubMembers = members.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toClubUiText()) } }
        }
    }

    /** F-DZ-05 — tirage équilibré simple (moitié/moitié) ; aucune donnée de poste/stats n'existe encore pour affiner. */
    private fun tirageEquilibre() {
        val melange = _state.value.membresAcceptes.shuffled()
        val milieu = melange.size / 2
        val assignments = melange.mapIndexed { index, membre ->
            membre.utilisateurId to if (index < milieu) CampSide.A else CampSide.B
        }.toMap()
        _state.update { it.copy(assignments = assignments) }
    }

    private fun valider() {
        val state = _state.value
        val campA = state.campA ?: return
        val campB = state.campB ?: return
        _state.update { it.copy(isSubmitting = true, errorMessage = null) }
        viewModelScope.launch {
            for ((utilisateurId, side) in state.assignments) {
                val campId = if (side == CampSide.A) campA.id else campB.id
                when (val result = addEffectifUseCase(campId, utilisateurId, numeroMaillot = null)) {
                    is Result.Failure -> {
                        _state.update { it.copy(isSubmitting = false, errorMessage = result.error.toUiText()) }
                        return@launch
                    }
                    is Result.Success -> Unit
                }
            }
            _state.update { it.copy(isSubmitting = false) }
            _events.send(RepartitionEvent.OnDone(campA, campB, state.clubId))
        }
    }
}
