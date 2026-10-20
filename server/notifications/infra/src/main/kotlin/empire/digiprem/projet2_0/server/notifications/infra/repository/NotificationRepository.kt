package empire.digiprem.projet2_0.server.notifications.infra.repository

import empire.digiprem.projet2_0.server.notifications.infra.entity.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NotificationRepository : JpaRepository<NotificationEntity, UUID> {
    fun findByUser_IdOrderByCreatedAtDesc(userId: UUID): List<NotificationEntity>
    fun findByUser_IdAndIdIn(userId: UUID, ids: List<UUID>): List<NotificationEntity>
}
