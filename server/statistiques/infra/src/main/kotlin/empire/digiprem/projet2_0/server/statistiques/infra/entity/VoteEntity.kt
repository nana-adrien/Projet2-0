package empire.digiprem.projet2_0.server.statistiques.infra.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

/** CU24 — vote "homme du match" déposé par un joueur pour un match donné.
 * [votantId]/[candidatId] résolvent la lacune UML documentée en tête de V1__schema_initial.sql
 * (migration V4) : aucune référence au votant ni au candidat n'existait jusqu'ici. */
@Entity
@Table(name = "vote")
class VoteEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    val matchId: UUID,

    val votantId: UUID,

    val candidatId: UUID,

    val horodatage: Instant = Instant.now(),
)
