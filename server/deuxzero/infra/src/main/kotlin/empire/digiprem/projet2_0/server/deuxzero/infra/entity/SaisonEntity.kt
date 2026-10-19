package empire.digiprem.projet2_0.server.deuxzero.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

/** RG-17 : camp champion = plus de victoires, puis différence de buts cumulée, puis buts
 * marqués, puis résultat du dernier match. RG-18 : saison clôturée = figée. */
@Entity
@Table(name = "saison")
class SaisonEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val deuxZeroId: UUID,

    @Column(nullable = false, columnDefinition = "text")
    val libelle: String,

    @Column(nullable = false)
    val dateDebut: LocalDate,

    val dateFin: LocalDate? = null,

    @Column(nullable = false, columnDefinition = "text")
    val statut: String = "A_VENIR",

    val campChampionId: UUID? = null,
)
