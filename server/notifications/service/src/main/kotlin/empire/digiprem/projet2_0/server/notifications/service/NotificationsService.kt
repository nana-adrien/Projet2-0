package empire.digiprem.projet2_0.server.notifications.service

import empire.digiprem.projet2_0.contracts.notifications.NotificationDto
import empire.digiprem.projet2_0.server.common.exception.UserNotFoundException
import empire.digiprem.projet2_0.server.notifications.infra.entity.NotificationEntity
import empire.digiprem.projet2_0.server.notifications.infra.repository.NotificationRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NotificationsService(
    private val notificationRepository: NotificationRepository,
) {

    fun getNotifications(userId: String): List<NotificationDto> {
        val uuid = UUID.fromString(userId)
        return notificationRepository.findByUser_IdOrderByCreatedAtDesc(uuid)
            .map { it.toDto() }
    }

    /** @Transactional : accès à la relation LAZY NotificationEntity.user. */
    @Transactional
    fun markRead(userId: String, id: String) {
        val userUuid = UUID.fromString(userId)
        val notificationUuid = UUID.fromString(id)
        val notification = notificationRepository.findById(notificationUuid)
            .filter { it.user.id == userUuid }
            .orElseThrow { UserNotFoundException() }
        notificationRepository.save(
            NotificationEntity(
                id = notification.id,
                user = notification.user,
                title = notification.title,
                body = notification.body,
                isRead = true,
                createdAt = notification.createdAt,
            )
        )
    }

    @Transactional
    fun markAllRead(userId: String) {
        val uuid = UUID.fromString(userId)
        val unread = notificationRepository.findByUser_IdOrderByCreatedAtDesc(uuid)
            .filter { !it.isRead }
        val updated = unread.map {
            NotificationEntity(
                id = it.id,
                user = it.user,
                title = it.title,
                body = it.body,
                isRead = true,
                createdAt = it.createdAt,
            )
        }
        notificationRepository.saveAll(updated)
    }

    private fun NotificationEntity.toDto() = NotificationDto(
        id = id.toString(),
        title = title,
        body = body,
        isRead = isRead,
        createdAt = createdAt.toString(),
    )
}
