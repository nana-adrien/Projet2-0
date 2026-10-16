package empire.digiprem.projet2_0.server.social.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

/** CU27 -- [contenuType]/[contenuId] resolvent la lacune UML "Signalement sans reference au
 * contenu signale" (cf. en-tete de V1__schema_initial.sql, colonnes ajoutees en V3 Jalon 6).
 * [auteurId] (deja present en V1) joue le role de signaleur. Back-office de traitement
 * (statut/decision par l'administrateur plateforme) hors scope mobile ET hors scope de ce jalon. */
@Entity
@Table(name = "signalement")
class SignalementEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val auteurId: UUID,

    @Column(nullable = false, columnDefinition = "text")
    val motif: String,

    @Column(nullable = false, columnDefinition = "text")
    val statut: String = "EN_ATTENTE",

    @Column(nullable = false)
    val dateCreation: Instant = Instant.now(),

    val dateTraitement: Instant? = null,

    @Column(columnDefinition = "text")
    val decision: String? = null,

    @Column(nullable = false, columnDefinition = "text")
    val contenuType: String,

    @Column(nullable = false)
    val contenuId: UUID,
)
