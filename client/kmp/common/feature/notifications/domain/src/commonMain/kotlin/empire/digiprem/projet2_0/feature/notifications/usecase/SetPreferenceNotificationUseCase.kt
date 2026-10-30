package empire.digiprem.projet2_0.feature.notifications.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.notifications.error.NotificationError
import empire.digiprem.projet2_0.feature.notifications.error.toNotificationError
import empire.digiprem.projet2_0.feature.notifications.model.NiveauNotification
import empire.digiprem.projet2_0.feature.notifications.model.PreferenceNotification
import empire.digiprem.projet2_0.feature.notifications.repository.INotificationRepository

/** E27 — Enregistrer les préférences de notification (CU26, F-NOT-02). */
class SetPreferenceNotificationUseCase(private val repository: INotificationRepository) {
    suspend operator fun invoke(deuxZeroId: String, niveau: NiveauNotification, neRienDerangerNocturne: Boolean): Result<PreferenceNotification?, NotificationError> =
        repository.setPreference(deuxZeroId, niveau, neRienDerangerNocturne).mapFailure { it.toNotificationError() }
}
