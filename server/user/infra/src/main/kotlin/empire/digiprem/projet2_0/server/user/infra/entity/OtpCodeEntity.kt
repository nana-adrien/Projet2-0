package empire.digiprem.projet2_0.server.user.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "otp_codes")
class OtpCodeEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, columnDefinition = "text")
    val contact: String,

    @Column(nullable = false, columnDefinition = "text")
    val purpose: String,

    @Column(nullable = false, columnDefinition = "text")
    val hashedCode: String,

    @Column(nullable = false)
    val attempts: Int = 0,

    @Column(nullable = false)
    val expiresAt: Instant,

    val lockedUntil: Instant? = null,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),
)
