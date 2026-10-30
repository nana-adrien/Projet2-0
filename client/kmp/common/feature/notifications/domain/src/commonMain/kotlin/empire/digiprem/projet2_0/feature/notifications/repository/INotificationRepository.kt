package empire.digiprem.projet2_0.feature.notifications.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.notifications.model.NiveauNotification
import empire.digiprem.projet2_0.feature.notifications.model.Notification
import empire.digiprem.projet2_0.feature.notifications.model.PreferenceNotification

interface INotificationRepository {
    suspend fun getNotifications(): Result<List<Notification>?, DataError.Remote>
    suspend fun markAsRead(id: String): Result<Unit?, DataError.Remote>
    suspend fun deleteNotification(id: String): Result<Unit?, DataError.Remote>
    suspend fun getPreference(deuxZeroId: String): Result<PreferenceNotification?, DataError.Remote>
    suspend fun setPreference(deuxZeroId: String, niveau: NiveauNotification, neRienDerangerNocturne: Boolean): Result<PreferenceNotification?, DataError.Remote>
}
