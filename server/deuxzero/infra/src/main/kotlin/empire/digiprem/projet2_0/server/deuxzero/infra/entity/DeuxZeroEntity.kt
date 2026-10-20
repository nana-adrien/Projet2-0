package empire.digiprem.projet2_0.server.deuxzero.infra.entity

import empire.digiprem.projet2_0.server.user.infra.entity.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalTime
import java.util.UUID

/** RG-04 : exactement 2 camps par 2-0 (enforcé en application).
 * RG-10 : durée de mi-temps propre au 2-0, modifiable ponctuellement par match (Jalon 3). */
@Entity
@Table(name = "deux_zero")
class DeuxZeroEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, columnDefinition = "text")
    val nom: String,

    @Column(columnDefinition = "text")
    val lieu: String? = null,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(nullable = false)
    val joursHabituels: List<String> = emptyList(),

    val heureHabituelle: LocalTime? = null,

    @Column(nullable = false)
    val dureeMiTempsMinutes: Int,

    @Column(nullable = false)
    val nombreMiTemps: Int,

    val dureePauseMinutes: Int? = null,

    @Column(nullable = false)
    val nombreMaxJoueursParCamp: Int,

    @Column(nullable = false)
    val delaiValidationAutomatiqueHeures: Int,

    val transfertsAutorisesParSaison: Int? = null,

    @Column(nullable = false, columnDefinition = "text")
    val visibilite: String = "PRIVE",

    @Column(nullable = false)
    val estInterne: Boolean = false,

    @Column(nullable = false, columnDefinition = "text")
    val statut: String = "CREATION",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisateur_id", nullable = false)
    val organisateur: UserEntity,
)
