package empire.digiprem.projet2_0.feature.auth.register_profile

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.auth.extension.toUiText
import empire.digiprem.projet2_0.feature.auth.usecase.RegisterUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E04 — Profil et code de club : dernière étape de CU1 (pseudo, mot de passe facultatif, code club facultatif). */
class RegisterProfileViewModel(
    private val registerUseCase: RegisterUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(RegisterProfileState())
    val state = _state.asStateFlow()

    private val _events = Channel<RegisterProfileEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: RegisterProfileAction) {
        when (action) {
            is RegisterProfileAction.OnInit -> {
                if (hasInitialized) return
                hasInitialized = true
                _state.update { it.copy(contact = action.contact, verificationToken = action.verificationToken) }
            }
            is RegisterProfileAction.OnPseudoChange -> _state.update {
                it.copy(pseudo = action.value, isFormValid = isValid(action.value, it.password))
            }
            is RegisterProfileAction.OnPasswordChange -> _state.update {
                it.copy(password = action.value, isFormValid = isValid(it.pseudo, action.value))
            }
            RegisterProfileAction.OnTogglePasswordVisibility -> _state.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            is RegisterProfileAction.OnClubCodeChange -> _state.update { it.copy(clubCode = action.value) }
            RegisterProfileAction.OnSubmit -> register()
        }
    }

    private fun isValid(pseudo: String, password: String): Boolean =
        pseudo.trim().length >= 3 && (password.isBlank() || password.length >= 8)

    private fun register() {
        val current = _state.value
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            registerUseCase(
                contact = current.contact,
                pseudo = current.pseudo.trim(),
                verificationToken = current.verificationToken,
                password = current.password.takeIf { it.isNotBlank() },
                clubCode = current.clubCode.trim().takeIf { it.isNotBlank() },
            )
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(RegisterProfileEvent.OnRegisterSuccess)
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) }
                }
        }
    }
}
