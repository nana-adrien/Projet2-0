package empire.digiprem.projet2_0.feature.notifications.datasource

import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.core.data.networking.delete
import empire.digiprem.projet2_0.core.data.networking.get
import empire.digiprem.projet2_0.core.data.networking.put
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map
import empire.digiprem.projet2_0.feature.notifications.dto.NotificationDto
import empire.digiprem.projet2_0.feature.notifications.dto.PreferenceNotificationDto
import empire.digiprem.projet2_0.feature.notifications.dto.SetPreferenceNotificationRequest
import empire.digiprem.projet2_0.feature.notifications.mapper.toDomain
import empire.digiprem.projet2_0.feature.notifications.model.NiveauNotification
import empire.digiprem.projet2_0.feature.notifications.model.Notification
import empire.digiprem.projet2_0.feature.notifications.model.PreferenceNotification
import io.ktor.client.HttpClient

class NotificationKtorDataSource(
    private val client: HttpClient,
) : INotificationDataSource {

    override suspend fun getNotifications(): Result<List<Notification>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<NotificationDto>>>(route = "v1/notifications")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun markAsRead(id: String): Result<Unit?, DataError.Remote> =
        client.put<Unit, ApiResponseWithPayload<Unit>>(
            route = "v1/notifications/$id/read",
            body  = Unit,
        ).map { it.payload }

    override suspend fun deleteNotification(id: String): Result<Unit?, DataError.Remote> =
        client.delete<ApiResponseWithPayload<Unit>>(route = "v1/notifications/$id")
            .map { it.payload }

    override suspend fun getPreference(deuxZeroId: String): Result<PreferenceNotification?, DataError.Remote> =
        client.get<ApiResponseWithPayload<PreferenceNotificationDto>>(route = "v1/deuxzero/$deuxZeroId/preference-notification")
            .map { it.payload?.toDomain() }

    override suspend fun setPreference(deuxZeroId: String, niveau: NiveauNotification, neRienDerangerNocturne: Boolean): Result<PreferenceNotification?, DataError.Remote> =
        client.put<SetPreferenceNotificationRequest, ApiResponseWithPayload<PreferenceNotificationDto>>(
            route = "v1/deuxzero/$deuxZeroId/preference-notification",
            body = SetPreferenceNotificationRequest(niveau.name, neRienDerangerNocturne),
        ).map { it.payload?.toDomain() }
}
