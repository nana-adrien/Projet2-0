package empire.digiprem.projet2_0.feature.notifications.repository

import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.notifications.datasource.INotificationDataSource
import empire.digiprem.projet2_0.feature.notifications.model.NiveauNotification
import empire.digiprem.projet2_0.feature.notifications.model.Notification
import empire.digiprem.projet2_0.feature.notifications.model.PreferenceNotification

class NotificationRepository(
    private val dataSource: INotificationDataSource,
) : INotificationRepository {

    override suspend fun getNotifications(): Result<List<Notification>?, DataError.Remote> =
        dataSource.getNotifications()

    override suspend fun markAsRead(id: String): Result<Unit?, DataError.Remote> =
        dataSource.markAsRead(id)

    override suspend fun deleteNotification(id: String): Result<Unit?, DataError.Remote> =
        dataSource.deleteNotification(id)

    override suspend fun getPreference(deuxZeroId: String): Result<PreferenceNotification?, DataError.Remote> =
        dataSource.getPreference(deuxZeroId)

    override suspend fun setPreference(deuxZeroId: String, niveau: NiveauNotification, neRienDerangerNocturne: Boolean): Result<PreferenceNotification?, DataError.Remote> =
        dataSource.setPreference(deuxZeroId, niveau, neRienDerangerNocturne)
}
