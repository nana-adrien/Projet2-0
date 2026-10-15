package empire.digiprem.projet2_0.feature.profile.profile

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.profile.extension.toUiText
import empire.digiprem.projet2_0.feature.profile.model.Profile
import empire.digiprem.projet2_0.feature.profile.usecase.DeleteAccountUseCase
import empire.digiprem.projet2_0.feature.profile.usecase.GetProfileUseCase
import empire.digiprem.projet2_0.feature.profile.usecase.UpdateProfileUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** CU4 — Modifier son profil (et supprimer son compte). */
class ProfileViewModel(
    private val getProfileUseCase    : GetProfileUseCase,
    private val updateProfileUseCase : UpdateProfileUseCase,
    private val deleteAccountUseCase : DeleteAccountUseCase,
) : AbstractViewModel() {

    private var id: String = ""

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _events = Channel<ProfileEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadProfile()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnRetry                   -> loadProfile()
            is ProfileAction.OnPseudoChange          -> _state.update { it.copy(pseudo = action.value) }
            is ProfileAction.OnNomChange             -> _state.update { it.copy(nom = action.value) }
            is ProfileAction.OnPrenomChange          -> _state.update { it.copy(prenom = action.value) }
            is ProfileAction.OnDateNaissanceChange   -> _state.update { it.copy(dateNaissance = action.value) }
            is ProfileAction.OnPosteChange           -> _state.update { it.copy(postePrefere = action.value) }
            is ProfileAction.OnPiedFortChange        -> _state.update { it.copy(piedFort = action.value) }
            is ProfileAction.OnCacherNomReelChange   -> _state.update { it.copy(cacherNomReel = action.value) }
            is ProfileAction.OnCacherAgeChange       -> _state.update { it.copy(cacherAge = action.value) }
            ProfileAction.OnSaveClick                -> save()
            ProfileAction.OnDeleteAccountClick       -> _state.update { it.copy(showDeleteDialog = true) }
            ProfileAction.OnDismissDeleteDialog      -> _state.update { it.copy(showDeleteDialog = false) }
            ProfileAction.OnConfirmDeleteAccount     -> deleteAccount()
        }
    }

    private fun loadProfile() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getProfileUseCase()
                .onSuccess { profile ->
                    id = profile?.id.orEmpty()
                    _state.update {
                        it.copy(
                            isLoading     = false,
                            pseudo        = profile?.pseudo.orEmpty(),
                            photo         = profile?.photo,
                            nom           = profile?.nom.orEmpty(),
                            prenom        = profile?.prenom.orEmpty(),
                            dateNaissance = profile?.dateNaissance.orEmpty(),
                            postePrefere  = profile?.postePrefere,
                            piedFort      = profile?.piedFort,
                            cacherNomReel = profile?.cacherNomReel ?: false,
                            cacherAge     = profile?.cacherAge ?: false,
                        )
                    }
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun save() {
        val s = _state.value
        if (!s.isFormValid) return
        _state.update { it.copy(isSaving = true, errorMessage = null) }
        viewModelScope.launch {
            updateProfileUseCase(
                Profile(
                    id            = id,
                    pseudo        = s.pseudo.trim(),
                    photo         = s.photo,
                    nom           = s.nom.trim().ifBlank { null },
                    prenom        = s.prenom.trim().ifBlank { null },
                    dateNaissance = s.dateNaissance.trim().ifBlank { null },
                    postePrefere  = s.postePrefere,
                    piedFort      = s.piedFort,
                    cacherNomReel = s.cacherNomReel,
                    cacherAge     = s.cacherAge,
                )
            )
                .onSuccess {
                    _state.update { it.copy(isSaving = false) }
                    _events.send(ProfileEvent.OnProfileUpdated)
                }
                .onFailure { error -> _state.update { it.copy(isSaving = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun deleteAccount() {
        _state.update { it.copy(isDeleting = true, showDeleteDialog = false, errorMessage = null) }
        viewModelScope.launch {
            deleteAccountUseCase()
                .onSuccess {
                    _state.update { it.copy(isDeleting = false) }
                    _events.send(ProfileEvent.OnAccountDeleted)
                }
                .onFailure { error -> _state.update { it.copy(isDeleting = false, errorMessage = error.toUiText()) } }
        }
    }
}
