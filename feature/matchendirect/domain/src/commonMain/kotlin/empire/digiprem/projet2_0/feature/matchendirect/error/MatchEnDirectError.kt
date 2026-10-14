package empire.digiprem.projet2_0.feature.matchendirect.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface MatchEnDirectError : ResultError {
    data object NotFound    : MatchEnDirectError
    data object Forbidden   : MatchEnDirectError
    data object Conflict    : MatchEnDirectError
    data object InvalidData : MatchEnDirectError
    data object Unknown     : MatchEnDirectError
}
