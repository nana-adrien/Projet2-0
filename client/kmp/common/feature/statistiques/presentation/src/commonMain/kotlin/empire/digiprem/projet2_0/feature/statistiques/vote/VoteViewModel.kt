package empire.digiprem.projet2_0.feature.statistiques.vote

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import empire.digiprem.projet2_0.feature.statistiques.extension.toUiText
import empire.digiprem.projet2_0.feature.statistiques.usecase.GetVoteHommeDuMatchUseCase
import empire.digiprem.projet2_0.feature.statistiques.usecase.VoterHommeDuMatchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E20 — Vote homme du match (CU24). */
class VoteViewModel(
    private val listCampsUseCase: ListCampsUseCase,
    private val getVoteHommeDuMatchUseCase: GetVoteHommeDuMatchUseCase,
    private val voterHommeDuMatchUseCase: VoterHommeDuMatchUseCase,
    private val authSessionStorage: AuthSessionStorage,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(VoteState())
    val state = _state.asStateFlow()

    private var hasInitialized = false

    fun onAction(action: VoteAction) {
        when (action) {
            is VoteAction.OnInit -> init(action.matchId, action.deuxZeroId)
            VoteAction.OnRetry -> charger()
            is VoteAction.OnCandidatClick -> _state.update { it.copy(candidatSelectionneId = action.utilisateurId) }
            VoteAction.OnVoterClick -> voter()
            VoteAction.OnMessageShown -> _state.update { it.copy(message = null) }
        }
    }

    private fun init(matchId: String, deuxZeroId: String) {
        _state.update { it.copy(matchId = matchId, deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch {
            _state.update { it.copy(currentUserId = authSessionStorage.getData()?.userId) }

            listCampsUseCase(deuxZeroId)
                .onSuccess { camps -> _state.update { it.copy(campA = camps?.getOrNull(0), campB = camps?.getOrNull(1)) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toDeuxZeroUiText()) } }

            charger()
        }
    }

    private fun charger() {
        val matchId = _state.value.matchId
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getVoteHommeDuMatchUseCase(matchId)
                .onSuccess { vote -> _state.update { it.copy(isLoading = false, vote = vote, candidatSelectionneId = vote?.monVoteCandidatId) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun voter() {
        val matchId = _state.value.matchId
        val candidatId = _state.value.candidatSelectionneId ?: return
        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            voterHommeDuMatchUseCase(matchId, candidatId)
                .onSuccess { vote -> _state.update { it.copy(isActionLoading = false, vote = vote, message = UiText.DynamicString("Vote enregistré.")) } }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }
}
