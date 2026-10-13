package empire.digiprem.projet2_0.server.clubs.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

/** RG-02 : un joueur peut être membre d'un nombre illimité de clubs.
 * RG-03 : un club peut être engagé dans un nombre illimité de 2-0. */
@Entity
@Table(name = "club")
class ClubEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, columnDefinition = "text")
    val nom: String,

    @Column(columnDefinition = "text")
    val logo: String? = null,

    @Column(columnDefinition = "text")
    val couleurs: String? = null,

    @Column(columnDefinition = "text")
    val quartier: String? = null,

    @Column(columnDefinition = "text")
    val description: String? = null,

    @Column(nullable = false, unique = true, columnDefinition = "text")
    val codeAdhesion: String,

    @Column(nullable = false, columnDefinition = "text")
    val statut: String = "ACTIF",

    val acceptationAutomatique: Boolean = false,

    val delegationCapitaines: Boolean = false,
)
