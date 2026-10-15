package empire.digiprem.projet2_0.feature.notifications.notifications

sealed interface NotificationsAction {
    data object OnRefresh                             : NotificationsAction
    data class OnMarkAsRead(val id: String)           : NotificationsAction
    data class OnDeleteNotification(val id: String)   : NotificationsAction
}
