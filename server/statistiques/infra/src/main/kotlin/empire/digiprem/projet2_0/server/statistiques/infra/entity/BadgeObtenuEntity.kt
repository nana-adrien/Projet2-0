package empire.digiprem.projet2_0.server.statistiques.infra.entity

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

/** CU25 — badge obtenu par un utilisateur (bilan de saison, progression).
 * Choix UML délibéré : pas de référence à un match. Le diagramme
 * uml/domain/commun/classe/modele-domaine.puml ne modélise que
 * Utilisateur "1" -- "0..*" BadgeObtenu ; le champ [contexte] (texte libre) est le moyen prévu
 * pour décrire le contexte d'obtention, ce n'est pas une FK à inventer. Table déjà complète
 * depuis V1__schema_initial.sql, aucune migration nécessaire pour ce jalon. */
@Entity
@Table(name = "badge_obtenu")
class BadgeObtenuEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    val utilisateur: UserEntity,

    @Column(nullable = false, columnDefinition = "text")
    val type: String,

    @Column(columnDefinition = "text")
    val contexte: String? = null,

    val date: Instant = Instant.now(),
)
