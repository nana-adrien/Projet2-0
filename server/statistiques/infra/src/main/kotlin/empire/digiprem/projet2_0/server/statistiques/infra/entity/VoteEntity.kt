package empire.digiprem.projet2_0.server.statistiques.infra.entity

import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.user.infra.entity.UserEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

/** CU24 — vote "homme du match" déposé par un joueur pour un match donné.
 * [votant]/[candidat] résolvent la lacune UML documentée en tête de V1__schema_initial.sql
 * (migration V4) : aucune référence au votant ni au candidat n'existait jusqu'ici. */
@Entity
@Table(name = "vote")
class VoteEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    val match: MatchEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "votant_id", nullable = false)
    val votant: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidat_id", nullable = false)
    val candidat: UserEntity,

    val horodatage: Instant = Instant.now(),
)
