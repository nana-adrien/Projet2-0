package empire.digiprem.projet2_0.contracts.notifications

import kotlinx.serialization.Serializable

@Serializable
data class MarkNotificationsReadRequest(
    val ids: List<String>,
)
