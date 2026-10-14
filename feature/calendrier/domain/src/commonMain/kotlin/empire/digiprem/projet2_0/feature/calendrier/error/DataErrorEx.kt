package empire.digiprem.projet2_0.feature.calendrier.error

import empire.digiprem.projet2_0.core.domain.network.DataError

fun DataError.Remote.toCalendrierError(): CalendrierError = when (this) {
    DataError.Remote.NotFound   -> CalendrierError.NotFound
    DataError.Remote.Forbidden  -> CalendrierError.Forbidden
    DataError.Remote.Conflict   -> CalendrierError.Conflict
    DataError.Remote.BadRequest -> CalendrierError.InvalidData
    else                        -> CalendrierError.Unknown
}
