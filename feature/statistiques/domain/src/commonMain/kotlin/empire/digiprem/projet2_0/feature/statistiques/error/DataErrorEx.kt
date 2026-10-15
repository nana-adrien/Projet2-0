package empire.digiprem.projet2_0.feature.statistiques.error

import empire.digiprem.projet2_0.core.domain.network.DataError

fun DataError.Remote.toStatistiquesError(): StatistiquesError = when (this) {
    DataError.Remote.NotFound   -> StatistiquesError.NotFound
    DataError.Remote.Forbidden  -> StatistiquesError.Forbidden
    DataError.Remote.Conflict   -> StatistiquesError.Conflict
    DataError.Remote.BadRequest -> StatistiquesError.InvalidData
    else                        -> StatistiquesError.Unknown
}
