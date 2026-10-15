package empire.digiprem.projet2_0.feature.notifications.mapper

import empire.digiprem.projet2_0.feature.notifications.dto.NotificationDto
import empire.digiprem.projet2_0.feature.notifications.model.Notification

fun NotificationDto.toDomain(): Notification = Notification(
    id        = id,
    title     = title,
    body      = body,
    isRead    = isRead,
    createdAt = createdAt,
    type      = type,
)
