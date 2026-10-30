package empire.digiprem.projet2_0.feature.clubs.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface ClubError : ResultError {
    data object NotFound    : ClubError
    data object Forbidden   : ClubError
    data object Conflict    : ClubError
    data object InvalidCode : ClubError
    data object Unknown     : ClubError
}
