package empire.digiprem.projet2_0.feature.validation.error

import empire.digiprem.projet2_0.core.domain.network.DataError

fun DataError.Remote.toValidationError(): ValidationError = when (this) {
    DataError.Remote.NotFound   -> ValidationError.NotFound
    DataError.Remote.Forbidden  -> ValidationError.Forbidden
    DataError.Remote.Conflict   -> ValidationError.Conflict
    DataError.Remote.BadRequest -> ValidationError.InvalidData
    else                        -> ValidationError.Unknown
}
