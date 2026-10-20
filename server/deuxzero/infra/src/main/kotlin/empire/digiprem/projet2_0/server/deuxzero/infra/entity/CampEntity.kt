package empire.digiprem.projet2_0.server.deuxzero.infra.entity

import empire.digiprem.projet2_0.server.clubs.infra.entity.ClubEntity
import empire.digiprem.projet2_0.server.user.infra.entity.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

/** RG-05 : les 2 camps d'un 2-0 peuvent être portés par le même club (2-0 interne).
 * [club] est nul jusqu'à l'engagement du club (CU9). */
@Entity
@Table(name = "camp")
class CampEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deux_zero_id", nullable = false)
    val deuxZero: DeuxZeroEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    val club: ClubEntity? = null,

    @Column(nullable = false, columnDefinition = "text")
    val libelle: String,

    @Column(nullable = false, unique = true, columnDefinition = "text")
    val codeEngagement: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capitaine_id")
    val capitaine: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vice_capitaine_id")
    val viceCapitaine: UserEntity? = null,
)
