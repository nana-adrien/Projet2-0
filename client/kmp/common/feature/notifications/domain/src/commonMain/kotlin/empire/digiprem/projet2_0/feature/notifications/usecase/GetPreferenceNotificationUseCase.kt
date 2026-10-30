package empire.digiprem.projet2_0.feature.notifications.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.notifications.error.NotificationError
import empire.digiprem.projet2_0.feature.notifications.error.toNotificationError
import empire.digiprem.projet2_0.feature.notifications.model.PreferenceNotification
import empire.digiprem.projet2_0.feature.notifications.repository.INotificationRepository

/** E27 — Préférences de notification (CU26). */
class GetPreferenceNotificationUseCase(private val repository: INotificationRepository) {
    suspend operator fun invoke(deuxZeroId: String): Result<PreferenceNotification?, NotificationError> =
        repository.getPreference(deuxZeroId).mapFailure { it.toNotificationError() }
}
