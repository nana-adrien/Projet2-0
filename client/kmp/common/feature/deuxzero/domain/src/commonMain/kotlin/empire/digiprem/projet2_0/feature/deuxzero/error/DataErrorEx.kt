package empire.digiprem.projet2_0.feature.deuxzero.error

import empire.digiprem.projet2_0.core.domain.network.DataError

fun DataError.Remote.toDeuxZeroError(): DeuxZeroError = when (this) {
    DataError.Remote.NotFound   -> DeuxZeroError.NotFound
    DataError.Remote.Forbidden  -> DeuxZeroError.Forbidden
    DataError.Remote.Conflict   -> DeuxZeroError.Conflict
    DataError.Remote.BadRequest -> DeuxZeroError.InvalidCode
    else                        -> DeuxZeroError.Unknown
}
