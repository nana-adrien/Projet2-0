package empire.digiprem.projet2_0.feature.notifications.notifications

sealed interface NotificationsEvent {
    data object OnError : NotificationsEvent
}
