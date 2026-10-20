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
import java.time.LocalDate
import java.util.UUID

/** RG-06/RG-07/RG-08/RG-09 : voir ClubService et DeuxZeroService (contrôles applicatifs). */
@Entity
@Table(name = "effectif_engage")
class EffectifEngageEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id", nullable = false)
    val camp: CampEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    val utilisateur: UserEntity,

    val numeroMaillot: Int? = null,

    @Column(nullable = false)
    val dateEntree: LocalDate = LocalDate.now(),

    val dateSortie: LocalDate? = null,
)
