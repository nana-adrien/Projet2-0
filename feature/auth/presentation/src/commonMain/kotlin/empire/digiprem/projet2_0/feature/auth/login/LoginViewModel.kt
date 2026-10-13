package empire.digiprem.projet2_0.feature.auth.login

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.auth.extension.toUiText
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose
import empire.digiprem.projet2_0.feature.auth.usecase.LoginUseCase
import empire.digiprem.projet2_0.feature.auth.usecase.RequestOtpUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** CU2 — Se connecter (mot de passe ou, via les liens, code à usage unique). */
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val requestOtpUseCase: RequestOtpUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnContactChange -> _state.update {
                it.copy(contact = action.value, isFormValid = isValid(action.value, it.password))
            }
            is LoginAction.OnPasswordChange -> _state.update {
                it.copy(password = action.value, isFormValid = isValid(it.contact, action.value))
            }
            LoginAction.OnTogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            LoginAction.OnLoginClick -> login()
            LoginAction.OnLoginWithCodeClick -> requestOtp(OtpPurpose.LOGIN)
            LoginAction.OnForgotPasswordClick -> requestOtp(OtpPurpose.PASSWORD_RESET)
        }
    }

    private fun isValid(contact: String, password: String): Boolean =
        contact.isNotBlank() && password.length >= 8

    private fun login() {
        val current = _state.value
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            loginUseCase(current.contact, current.password)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(LoginEvent.OnLoginSuccess)
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) }
                }
        }
    }

    private fun requestOtp(purpose: OtpPurpose) {
        val contact = _state.value.contact.trim()
        if (contact.isBlank()) return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            requestOtpUseCase(contact, purpose)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(LoginEvent.OnOtpRequested(contact, purpose))
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) }
                }
        }
    }
}
