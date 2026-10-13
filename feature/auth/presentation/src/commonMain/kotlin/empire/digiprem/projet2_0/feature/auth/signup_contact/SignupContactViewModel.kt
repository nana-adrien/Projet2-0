package empire.digiprem.projet2_0.feature.auth.signup_contact

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.auth.extension.toUiText
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose
import empire.digiprem.projet2_0.feature.auth.usecase.RequestOtpUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E02 — Numéro de téléphone (ou e-mail), étape 1 de CU1. */
class SignupContactViewModel(
    private val requestOtpUseCase: RequestOtpUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(SignupContactState())
    val state = _state.asStateFlow()

    private val _events = Channel<SignupContactEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SignupContactAction) {
        when (action) {
            is SignupContactAction.OnContactChange -> _state.update {
                it.copy(contact = action.value, errorMessage = null, isFormValid = isValid(action.value, it.isEmailMode))
            }
            SignupContactAction.OnToggleEmailMode -> _state.update {
                val nowEmail = !it.isEmailMode
                it.copy(isEmailMode = nowEmail, contact = "", isFormValid = false)
            }
            SignupContactAction.OnSubmit -> requestOtp()
        }
    }

    private fun isValid(contact: String, isEmailMode: Boolean): Boolean =
        if (isEmailMode) contact.contains("@") && contact.length >= 5
        else contact.length >= 8

    private fun requestOtp() {
        val contact = _state.value.contact.trim()
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            requestOtpUseCase(contact, OtpPurpose.SIGNUP)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(SignupContactEvent.OnOtpSent(contact))
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) }
                }
        }
    }
}
