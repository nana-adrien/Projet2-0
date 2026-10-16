package empire.digiprem.projet2_0.server.notifications.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

/** CU26 -- une preference par couple (utilisateurId, deuxZeroId), cf. contrainte unique en base
 * (table deja presente en V1, cf. en-tete de V1__schema_initial.sql). */
@Entity
@Table(name = "preference_notification")
class PreferenceNotificationEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val utilisateurId: UUID,

    @Column(nullable = false)
    val deuxZeroId: UUID,

    @Column(nullable = false, columnDefinition = "text")
    val niveau: String = "TOUT",

    @Column(nullable = false)
    val neRienDerangerNocturne: Boolean = false,
)
