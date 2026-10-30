package empire.digiprem.projet2_0.feature.notifications.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val id        : String,
    val title     : String,
    val body      : String,
    val isRead    : Boolean = false,
    val createdAt : Long    = 0L,
    val type      : String  = "",
)
