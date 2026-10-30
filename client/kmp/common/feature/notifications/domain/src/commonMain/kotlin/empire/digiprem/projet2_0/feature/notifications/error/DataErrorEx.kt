package empire.digiprem.projet2_0.feature.notifications.error

import empire.digiprem.projet2_0.core.domain.network.DataError

fun DataError.Remote.toNotificationError(): NotificationError = when (this) {
    DataError.Remote.NotFound   -> NotificationError.NotFound
    DataError.Remote.Forbidden  -> NotificationError.Forbidden
    DataError.Remote.BadRequest -> NotificationError.InvalidData
    else                        -> NotificationError.Unknown
}
