package empire.digiprem.projet2_0.server.clubs.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "adhesion_club")
class AdhesionClubEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val utilisateurId: UUID,

    @Column(nullable = false)
    val clubId: UUID,

    @Column(nullable = false, columnDefinition = "text")
    val role: String = "MEMBRE",

    @Column(nullable = false, columnDefinition = "text")
    val statut: String = "EN_ATTENTE",

    @Column(nullable = false)
    val dateDemande: Instant = Instant.now(),

    val dateReponse: Instant? = null,
)
