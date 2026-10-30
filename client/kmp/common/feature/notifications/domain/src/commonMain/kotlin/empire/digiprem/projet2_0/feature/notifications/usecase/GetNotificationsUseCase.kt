package empire.digiprem.projet2_0.feature.notifications.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.notifications.error.NotificationError
import empire.digiprem.projet2_0.feature.notifications.model.Notification
import empire.digiprem.projet2_0.feature.notifications.repository.INotificationRepository

class GetNotificationsUseCase(private val repository: INotificationRepository) {
    suspend operator fun invoke(): Result<List<Notification>?, NotificationError> =
        repository.getNotifications().mapFailure { NotificationError.Unknown }
}
