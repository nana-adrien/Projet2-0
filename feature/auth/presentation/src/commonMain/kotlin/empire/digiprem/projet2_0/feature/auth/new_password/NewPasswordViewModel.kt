package empire.digiprem.projet2_0.feature.auth.new_password

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.auth.extension.toUiText
import empire.digiprem.projet2_0.feature.auth.usecase.ResetPasswordUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** CU3 — Réinitialiser son mot de passe (dernière étape, après vérification du code). */
class NewPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(NewPasswordState())
    val state = _state.asStateFlow()

    private val _events = Channel<NewPasswordEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: NewPasswordAction) {
        when (action) {
            is NewPasswordAction.OnInit -> {
                if (hasInitialized) return
                hasInitialized = true
                _state.update { it.copy(contact = action.contact, verificationToken = action.verificationToken) }
            }
            is NewPasswordAction.OnPasswordChange -> _state.update {
                it.copy(newPassword = action.value, isFormValid = action.value.length >= 8)
            }
            NewPasswordAction.OnToggleVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            NewPasswordAction.OnSubmit -> submit()
        }
    }

    private fun submit() {
        val current = _state.value
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            resetPasswordUseCase(current.contact, current.verificationToken, current.newPassword)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(NewPasswordEvent.OnPasswordResetSuccess)
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) }
                }
        }
    }
}
