package empire.digiprem.projet2_0.feature.clubs.error

import empire.digiprem.projet2_0.core.domain.network.DataError

fun DataError.Remote.toClubError(): ClubError = when (this) {
    DataError.Remote.NotFound    -> ClubError.NotFound
    DataError.Remote.Forbidden   -> ClubError.Forbidden
    DataError.Remote.Conflict    -> ClubError.Conflict
    DataError.Remote.BadRequest  -> ClubError.InvalidCode
    else                         -> ClubError.Unknown
}
