package empire.digiprem.projet2_0.feature.auth.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface EmailValidationError : ResultError {
    data object Empty         : EmailValidationError
    data object InvalidFormat : EmailValidationError
}
