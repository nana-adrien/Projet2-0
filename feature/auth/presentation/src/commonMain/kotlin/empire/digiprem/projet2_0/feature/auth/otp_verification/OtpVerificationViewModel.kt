package empire.digiprem.projet2_0.feature.auth.otp_verification

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.auth.extension.toUiText
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose
import empire.digiprem.projet2_0.feature.auth.usecase.LoginWithOtpUseCase
import empire.digiprem.projet2_0.feature.auth.usecase.RequestOtpUseCase
import empire.digiprem.projet2_0.feature.auth.usecase.VerifyOtpUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val RESEND_COOLDOWN_SECONDS = 60
private const val OTP_LENGTH = 6

/** E03 — Code de vérification, réutilisé pour CU1 (inscription), CU2-A1 (connexion sans mot
 * de passe) et CU3 (réinitialisation de mot de passe) : seul [OtpPurpose] change le comportement. */
class OtpVerificationViewModel(
    private val requestOtpUseCase: RequestOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val loginWithOtpUseCase: LoginWithOtpUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(OtpVerificationState())
    val state = _state.asStateFlow()

    private val _events = Channel<OtpVerificationEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: OtpVerificationAction) {
        when (action) {
            is OtpVerificationAction.OnInit -> init(action.contact, action.purpose)
            is OtpVerificationAction.OnCodeChange -> onCodeChange(action.value)
            OtpVerificationAction.OnResendClick -> resend()
        }
    }

    private fun init(contact: String, purpose: OtpPurpose) {
        if (hasInitialized) return
        hasInitialized = true
        _state.update { it.copy(contact = contact, purpose = purpose) }
        startCooldown()
    }

    private fun onCodeChange(value: String) {
        _state.update { it.copy(code = value, errorMessage = null) }
        if (value.length == OTP_LENGTH) verify(value)
    }

    private fun verify(code: String) {
        val current = _state.value
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            verifyOtpUseCase(current.contact, current.purpose, code)
                .onSuccess { token -> onVerified(current.contact, current.purpose, token.orEmpty()) }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, code = "", errorMessage = error.toUiText()) }
                }
        }
    }

    private suspend fun onVerified(contact: String, purpose: OtpPurpose, verificationToken: String) {
        when (purpose) {
            OtpPurpose.SIGNUP -> {
                _state.update { it.copy(isLoading = false) }
                _events.send(OtpVerificationEvent.OnVerifiedForSignup(contact, verificationToken))
            }
            OtpPurpose.PASSWORD_RESET -> {
                _state.update { it.copy(isLoading = false) }
                _events.send(OtpVerificationEvent.OnVerifiedForPasswordReset(contact, verificationToken))
            }
            OtpPurpose.LOGIN -> {
                loginWithOtpUseCase(contact, verificationToken)
                    .onSuccess {
                        _state.update { it.copy(isLoading = false) }
                        _events.send(OtpVerificationEvent.OnLoggedIn)
                    }
                    .onFailure { error ->
                        _state.update { it.copy(isLoading = false, code = "", errorMessage = error.toUiText()) }
                    }
            }
        }
    }

    private fun resend() {
        val current = _state.value
        if (current.secondsBeforeResend > 0) return
        viewModelScope.launch {
            requestOtpUseCase(current.contact, current.purpose)
                .onSuccess { startCooldown() }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toUiText()) } }
        }
    }

    private fun startCooldown() {
        _state.update { it.copy(secondsBeforeResend = RESEND_COOLDOWN_SECONDS) }
        viewModelScope.launch {
            while (_state.value.secondsBeforeResend > 0) {
                delay(1_000)
                _state.update { it.copy(secondsBeforeResend = it.secondsBeforeResend - 1) }
            }
        }
    }
}
