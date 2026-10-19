package empire.digiprem.projet2_0.server.deuxzero.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

/** RG-06/RG-07/RG-08/RG-09 : voir ClubService et DeuxZeroService (contrôles applicatifs). */
@Entity
@Table(name = "effectif_engage")
class EffectifEngageEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val campId: UUID,

    @Column(nullable = false)
    val utilisateurId: UUID,

    val numeroMaillot: Int? = null,

    @Column(nullable = false)
    val dateEntree: LocalDate = LocalDate.now(),

    val dateSortie: LocalDate? = null,
)
