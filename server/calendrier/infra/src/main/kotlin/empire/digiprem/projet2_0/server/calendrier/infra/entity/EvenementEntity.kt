package empire.digiprem.projet2_0.server.calendrier.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

/** CU17 — un événement de la feuille de match (but, carton, remplacement...).
 * [campId]/[joueurId]/[joueurSecondaireId] résolvent la lacune UML documentée en tête de
 * V1__schema_initial.sql (migration V2). [joueurId] = buteur/porteur du carton/sortant selon
 * [type] ; [joueurSecondaireId] = passeur (BUT) ou entrant (REMPLACEMENT) ; les deux sont nuls
 * pour DEBUT_PERIODE/FIN_PERIODE. [clientEventId] = identifiant généré côté mobile pour la
 * resynchronisation hors connexion (F-LIV-06) ; le serveur ignore les doublons sur ce champ. */
@Entity
@Table(name = "evenement")
class EvenementEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    val matchId: UUID,

    @Column(nullable = false, columnDefinition = "text")
    val type: String,

    val minute: Int,

    val periode: Int,

    val campId: UUID? = null,

    val joueurId: UUID? = null,

    val joueurSecondaireId: UUID? = null,

    val auteurSaisieId: UUID,

    val clientEventId: UUID? = null,

    val horodatage: Instant = Instant.now(),

    val annule: Boolean = false,
)
