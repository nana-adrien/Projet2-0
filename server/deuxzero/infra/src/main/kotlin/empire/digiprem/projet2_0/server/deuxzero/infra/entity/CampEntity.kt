package empire.digiprem.projet2_0.server.deuxzero.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

/** RG-05 : les 2 camps d'un 2-0 peuvent être portés par le même club (2-0 interne).
 * [clubId] est nul jusqu'à l'engagement du club (CU9). */
@Entity
@Table(name = "camp")
class CampEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val deuxZeroId: UUID,

    val clubId: UUID? = null,

    @Column(nullable = false, columnDefinition = "text")
    val libelle: String,

    @Column(nullable = false, unique = true, columnDefinition = "text")
    val codeEngagement: String,

    val capitaineId: UUID? = null,

    val viceCapitaineId: UUID? = null,
)
