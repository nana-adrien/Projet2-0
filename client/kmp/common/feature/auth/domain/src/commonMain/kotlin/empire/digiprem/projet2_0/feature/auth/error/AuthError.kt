package empire.digiprem.projet2_0.feature.auth.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface AuthError : ResultError {
    data object InvalidCredentials  : AuthError
    data object AlreadyExists       : AuthError
    data object WeakPassword        : AuthError
    data object InvalidCode         : AuthError
    data object CodeExpired         : AuthError
    data object TooManyAttempts     : AuthError
    data object InvalidToken        : AuthError
    data object Unknown             : AuthError
}
