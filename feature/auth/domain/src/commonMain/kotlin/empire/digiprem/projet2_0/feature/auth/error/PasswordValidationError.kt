package empire.digiprem.projet2_0.feature.auth.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface PasswordValidationError : ResultError {
    data object Empty    : PasswordValidationError
    data object TooShort : PasswordValidationError
    data object NoDigit  : PasswordValidationError
}
