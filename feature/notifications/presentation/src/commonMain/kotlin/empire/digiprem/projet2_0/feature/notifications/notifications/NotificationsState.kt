package empire.digiprem.projet2_0.feature.notifications.notifications

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.notifications.model.Notification

data class NotificationsState(
    val notifications : List<Notification> = emptyList(),
    val isLoading     : Boolean            = false,
    val errorMessage  : UiText?            = null,
)
