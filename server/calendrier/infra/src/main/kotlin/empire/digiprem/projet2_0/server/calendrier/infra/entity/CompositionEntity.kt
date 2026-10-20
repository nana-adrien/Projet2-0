package empire.digiprem.projet2_0.server.calendrier.infra.entity

import empire.digiprem.projet2_0.server.user.infra.entity.UserEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

/** CU16 — composition d'équipe (titulaires/remplaçants) avant un match. */
@Entity
@Table(name = "composition")
class CompositionEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    val match: MatchEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    val utilisateur: UserEntity,

    val titulaire: Boolean = false,

    val present: Boolean = false,
)
