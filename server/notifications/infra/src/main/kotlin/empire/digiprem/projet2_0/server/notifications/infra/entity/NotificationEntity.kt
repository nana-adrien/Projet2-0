package empire.digiprem.projet2_0.server.notifications.infra.entity

import empire.digiprem.projet2_0.server.user.infra.entity.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "notifications")
class NotificationEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @Column(nullable = false, columnDefinition = "text")
    val title: String,

    @Column(nullable = false, columnDefinition = "text")
    val body: String,

    val isRead: Boolean = false,

    val createdAt: Instant = Instant.now(),
)
