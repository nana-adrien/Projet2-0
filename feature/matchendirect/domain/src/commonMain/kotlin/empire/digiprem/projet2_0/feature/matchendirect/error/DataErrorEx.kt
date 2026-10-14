package empire.digiprem.projet2_0.feature.matchendirect.error

import empire.digiprem.projet2_0.core.domain.network.DataError

fun DataError.Remote.toMatchEnDirectError(): MatchEnDirectError = when (this) {
    DataError.Remote.NotFound   -> MatchEnDirectError.NotFound
    DataError.Remote.Forbidden  -> MatchEnDirectError.Forbidden
    DataError.Remote.Conflict   -> MatchEnDirectError.Conflict
    DataError.Remote.BadRequest -> MatchEnDirectError.InvalidData
    else                        -> MatchEnDirectError.Unknown
}
