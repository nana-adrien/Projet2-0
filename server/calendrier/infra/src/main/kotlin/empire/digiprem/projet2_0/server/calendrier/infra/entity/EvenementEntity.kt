package empire.digiprem.projet2_0.server.calendrier.infra.entity

import empire.digiprem.projet2_0.server.deuxzero.infra.entity.CampEntity
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

/** CU17 — un événement de la feuille de match (but, carton, remplacement...).
 * [camp]/[joueur]/[joueurSecondaire] résolvent la lacune UML documentée en tête de
 * V1__schema_initial.sql (migration V2). [joueur] = buteur/porteur du carton/sortant selon
 * [type] ; [joueurSecondaire] = passeur (BUT) ou entrant (REMPLACEMENT) ; les deux sont nuls
 * pour DEBUT_PERIODE/FIN_PERIODE. [clientEventId] = identifiant généré côté mobile pour la
 * resynchronisation hors connexion (F-LIV-06) ; le serveur ignore les doublons sur ce champ. */
@Entity
@Table(name = "evenement")
class EvenementEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    val match: MatchEntity,

    @Column(nullable = false, columnDefinition = "text")
    val type: String,

    val minute: Int,

    val periode: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    val camp: CampEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "joueur_id")
    val joueur: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "joueur_secondaire_id")
    val joueurSecondaire: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_saisie_id", nullable = false)
    val auteurSaisie: UserEntity,

    val clientEventId: UUID? = null,

    val horodatage: Instant = Instant.now(),

    val annule: Boolean = false,
)
