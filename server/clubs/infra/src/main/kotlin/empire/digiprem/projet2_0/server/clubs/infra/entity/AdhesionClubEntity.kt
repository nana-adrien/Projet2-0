package empire.digiprem.projet2_0.server.clubs.infra.entity

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

@Entity
@Table(name = "adhesion_club")
class AdhesionClubEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    val utilisateur: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    val club: ClubEntity,

    @Column(nullable = false, columnDefinition = "text")
    val role: String = "MEMBRE",

    @Column(nullable = false, columnDefinition = "text")
    val statut: String = "EN_ATTENTE",

    @Column(nullable = false)
    val dateDemande: Instant = Instant.now(),

    val dateReponse: Instant? = null,
)
