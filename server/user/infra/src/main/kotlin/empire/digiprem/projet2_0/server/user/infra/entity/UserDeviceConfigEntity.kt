package empire.digiprem.projet2_0.server.user.infra.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "utilisateur_fcm_tokens")
class UserDeviceConfigEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,

    @Column(name = "device_id", nullable = false, unique = true)
    val deviceId: String,

    @Column(nullable = false)
    val platform: String, // ANDROID ou IOS

    @Column(name = "app_version", nullable = false)
    var appVersion: String, // ex: "1.2.0"

    @Column(nullable = false)
    var timezone: String, // ex: "Africa/Douala" ou "Europe/Paris"

    @Column(nullable = false, unique = true)
    var token: String,

    @Column(name = "last_used_at",nullable = false)
    var lastUsedAt: Instant = Instant.now()
)
