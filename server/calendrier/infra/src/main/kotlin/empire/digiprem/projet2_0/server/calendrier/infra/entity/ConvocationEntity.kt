package empire.digiprem.projet2_0.server.calendrier.infra.entity

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

/** RG : une convocation unique par couple (match, utilisateur) — contrainte unique en base.
 * [dateReponse] reste nul tant que le joueur n'a pas répondu (statut SANS_REPONSE). */
@Entity
@Table(name = "convocation")
class ConvocationEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    val match: MatchEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    val utilisateur: UserEntity,

    @Column(nullable = false, columnDefinition = "text")
    val reponse: String = "SANS_REPONSE",

    val dateReponse: Instant? = null,
)
