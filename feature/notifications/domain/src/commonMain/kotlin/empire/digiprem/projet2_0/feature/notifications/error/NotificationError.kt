package empire.digiprem.projet2_0.feature.notifications.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface NotificationError : ResultError {
    data object NotFound    : NotificationError
    data object Forbidden   : NotificationError
    data object InvalidData : NotificationError
    data object Unknown     : NotificationError
}
