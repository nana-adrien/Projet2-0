package empire.digiprem.projet2_0.feature.amical.error

import empire.digiprem.projet2_0.core.domain.network.DataError

fun DataError.Remote.toAmicalError(): AmicalError = when (this) {
    DataError.Remote.Forbidden  -> AmicalError.Forbidden
    DataError.Remote.BadRequest -> AmicalError.InvalidData
    else                        -> AmicalError.Unknown
}
