package empire.digiprem.projet2_0.feature.notifications.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.notifications.error.NotificationError
import empire.digiprem.projet2_0.feature.notifications.repository.INotificationRepository

class DeleteNotificationUseCase(private val repository: INotificationRepository) {
    suspend operator fun invoke(id: String): Result<Unit?, NotificationError> =
        repository.deleteNotification(id).mapFailure { NotificationError.Unknown }
}
