package empire.digiprem.projet2_0.feature.calendrier.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface CalendrierError : ResultError {
    data object NotFound    : CalendrierError
    data object Forbidden   : CalendrierError
    data object Conflict    : CalendrierError
    data object InvalidData : CalendrierError
    data object Unknown     : CalendrierError
}
