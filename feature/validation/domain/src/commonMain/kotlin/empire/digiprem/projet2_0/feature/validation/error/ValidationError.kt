package empire.digiprem.projet2_0.feature.validation.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface ValidationError : ResultError {
    data object NotFound    : ValidationError
    data object Forbidden   : ValidationError
    data object Conflict    : ValidationError
    data object InvalidData : ValidationError
    data object Unknown     : ValidationError
}
