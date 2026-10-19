package empire.digiprem.projet2_0.server.calendrier.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
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

    @Column(nullable = false)
    val matchId: UUID,

    @Column(nullable = false)
    val utilisateurId: UUID,

    @Column(nullable = false, columnDefinition = "text")
    val reponse: String = "SANS_REPONSE",

    val dateReponse: Instant? = null,
)
