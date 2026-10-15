package empire.digiprem.projet2_0.feature.statistiques.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface StatistiquesError : ResultError {
    data object NotFound    : StatistiquesError
    data object Forbidden   : StatistiquesError
    data object Conflict    : StatistiquesError
    data object InvalidData : StatistiquesError
    data object Unknown     : StatistiquesError
}
