package empire.digiprem.projet2_0.server.calendrier.infra.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

/** CU16 — composition d'équipe (titulaires/remplaçants) avant un match. */
@Entity
@Table(name = "composition")
class CompositionEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    val matchId: UUID,

    val utilisateurId: UUID,

    val titulaire: Boolean = false,

    val present: Boolean = false,
)
