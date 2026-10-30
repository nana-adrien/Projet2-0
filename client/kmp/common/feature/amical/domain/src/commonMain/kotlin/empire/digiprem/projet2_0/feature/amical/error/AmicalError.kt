package empire.digiprem.projet2_0.feature.amical.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface AmicalError : ResultError {
    data object Forbidden   : AmicalError
    data object InvalidData : AmicalError
    data object Unknown     : AmicalError
}
