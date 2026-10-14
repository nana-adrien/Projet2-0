package empire.digiprem.projet2_0.feature.clubs.gestion_club

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.clubs.extension.toUiText
import empire.digiprem.projet2_0.feature.clubs.model.MembershipRole
import empire.digiprem.projet2_0.feature.clubs.usecase.GetClubUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.LeaveClubUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.ListMembersUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.NameCoAdminUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.RegenerateCodeUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.RemoveMemberUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.RespondToRequestUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.TransferAdminUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.UpdateClubSettingsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E23 — Gestion du club (CU7). */
class GestionClubViewModel(
    private val getClubUseCase: GetClubUseCase,
    private val listMembersUseCase: ListMembersUseCase,
    private val respondToRequestUseCase: RespondToRequestUseCase,
    private val removeMemberUseCase: RemoveMemberUseCase,
    private val nameCoAdminUseCase: NameCoAdminUseCase,
    private val transferAdminUseCase: TransferAdminUseCase,
    private val regenerateCodeUseCase: RegenerateCodeUseCase,
    private val updateClubSettingsUseCase: UpdateClubSettingsUseCase,
    private val leaveClubUseCase: LeaveClubUseCase,
    private val authSessionStorage: AuthSessionStorage,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(GestionClubState())
    val state = _state.asStateFlow()

    private val _events = Channel<GestionClubEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false
    private lateinit var clubId: String

    fun onAction(action: GestionClubAction) {
        when (action) {
            is GestionClubAction.OnInit -> init(action.clubId, action.initialRole)
            GestionClubAction.OnRetry -> load()
            is GestionClubAction.OnRespondToRequest -> respondToRequest(action.targetUserId, action.accept)
            is GestionClubAction.OnRemoveMember -> removeMember(action.targetUserId)
            is GestionClubAction.OnNameCoAdmin -> nameCoAdmin(action.targetUserId)
            is GestionClubAction.OnTransferAdmin -> transferAdmin(action.targetUserId)
            GestionClubAction.OnRegenerateCodeClick -> regenerateCode()
            is GestionClubAction.OnToggleAcceptationAutomatique ->
                updateSettings(acceptationAutomatique = action.value, delegationCapitaines = null)
            is GestionClubAction.OnToggleDelegationCapitaines ->
                updateSettings(acceptationAutomatique = null, delegationCapitaines = action.value)
            GestionClubAction.OnLeaveClubClick -> leaveClub()
            GestionClubAction.OnMessageShown -> _state.update { it.copy(message = null) }
            GestionClubAction.OnCreateDeuxZeroClick -> viewModelScope.launch {
                _events.send(GestionClubEvent.OnNavigateToCreateDeuxZero(clubId))
            }
            GestionClubAction.OnEngagerClubClick -> viewModelScope.launch {
                _events.send(GestionClubEvent.OnNavigateToEngagerClub(clubId))
            }
        }
    }

    private fun init(clubId: String, initialRole: MembershipRole) {
        this.clubId = clubId
        _state.update { it.copy(isCurrentUserAdmin = initialRole == MembershipRole.ADMINISTRATEUR) }
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch { _state.update { it.copy(currentUserId = authSessionStorage.getData()?.userId) } }
        load()
    }

    private fun load() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getClubUseCase(clubId)
                .onSuccess { club -> _state.update { it.copy(club = club) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toUiText()) } }

            listMembersUseCase(clubId)
                .onSuccess { members -> _state.update { it.copy(isLoading = false, members = members.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun respondToRequest(targetUserId: String, accept: Boolean) {
        viewModelScope.launch {
            respondToRequestUseCase(clubId, targetUserId, accept)
                .onSuccess {
                    _state.update {
                        it.copy(message = UiText.DynamicString(if (accept) "Demande acceptée." else "Demande refusée."))
                    }
                    load()
                }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }

    private fun removeMember(targetUserId: String) {
        viewModelScope.launch {
            removeMemberUseCase(clubId, targetUserId)
                .onSuccess { _state.update { it.copy(message = UiText.DynamicString("Membre retiré.")) }; load() }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }

    private fun nameCoAdmin(targetUserId: String) {
        viewModelScope.launch {
            nameCoAdminUseCase(clubId, targetUserId)
                .onSuccess { _state.update { it.copy(message = UiText.DynamicString("Co-administrateur nommé.")) }; load() }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }

    private fun transferAdmin(targetUserId: String) {
        viewModelScope.launch {
            transferAdminUseCase(clubId, targetUserId)
                .onSuccess {
                    _state.update { it.copy(isCurrentUserAdmin = false, message = UiText.DynamicString("Rôle d'administrateur transféré.")) }
                    load()
                }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }

    private fun regenerateCode() {
        viewModelScope.launch {
            regenerateCodeUseCase(clubId)
                .onSuccess { club -> _state.update { it.copy(club = club, message = UiText.DynamicString("Code régénéré.")) } }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }

    private fun updateSettings(acceptationAutomatique: Boolean?, delegationCapitaines: Boolean?) {
        viewModelScope.launch {
            updateClubSettingsUseCase(clubId, acceptationAutomatique, delegationCapitaines)
                .onSuccess { club -> _state.update { it.copy(club = club) } }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }

    private fun leaveClub() {
        viewModelScope.launch {
            leaveClubUseCase(clubId)
                .onSuccess { _events.send(GestionClubEvent.OnLeftClub) }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }
}
