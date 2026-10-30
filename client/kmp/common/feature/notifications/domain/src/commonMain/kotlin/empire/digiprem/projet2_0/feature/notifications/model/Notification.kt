package empire.digiprem.projet2_0.feature.notifications.model

data class Notification(
    val id        : String,
    val title     : String,
    val body      : String,
    val isRead    : Boolean = false,
    val createdAt : Long    = 0L,
    val type      : String  = "",
)
