package empire.digiprem.projet2_0.feature.deuxzero.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface DeuxZeroError : ResultError {
    data object NotFound    : DeuxZeroError
    data object Forbidden   : DeuxZeroError
    data object Conflict    : DeuxZeroError
    data object InvalidCode : DeuxZeroError
    data object Unknown     : DeuxZeroError
}
