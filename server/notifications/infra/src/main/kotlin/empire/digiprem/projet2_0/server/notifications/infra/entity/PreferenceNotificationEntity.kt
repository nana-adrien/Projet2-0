package empire.digiprem.projet2_0.server.notifications.infra.entity

import empire.digiprem.projet2_0.server.deuxzero.infra.entity.DeuxZeroEntity
import empire.digiprem.projet2_0.server.user.infra.entity.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

/** CU26 -- une preference par couple (utilisateur, deuxZero), cf. contrainte unique en base
 * (table deja presente en V1, cf. en-tete de V1__schema_initial.sql). */
@Entity
@Table(name = "preference_notification")
class PreferenceNotificationEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    val utilisateur: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deux_zero_id", nullable = false)
    val deuxZero: DeuxZeroEntity,

    @Column(nullable = false, columnDefinition = "text")
    val niveau: String = "TOUT",

    @Column(nullable = false)
    val neRienDerangerNocturne: Boolean = false,
)
