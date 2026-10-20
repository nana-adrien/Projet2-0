package empire.digiprem.projet2_0.server.social.infra.entity

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

/** CU27 -- [contenuType]/[contenuId] resolvent la lacune UML "Signalement sans reference au
 * contenu signale" (cf. en-tete de V1__schema_initial.sql, colonnes ajoutees en V3 Jalon 6).
 * [auteur] (deja present en V1) joue le role de signaleur. [contenuId] reste un UUID brut
 * (référence polymorphe club/match/profil selon [contenuType]) -- impossible à typer en relation
 * JPA unique. Back-office de traitement (statut/decision par l'administrateur plateforme) hors
 * scope mobile ET hors scope de ce jalon. */
@Entity
@Table(name = "signalement")
class SignalementEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    val auteur: UserEntity,

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
