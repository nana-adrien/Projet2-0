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

/** CU19 — un capitaine/vice-capitaine signe la feuille de match (VALIDE/CONTESTE) pour son camp.
 * CU20 — en cas de litige (CONTESTE d'au moins un camp, ou expiration du délai RG-12), l'organisateur
 * du 2-0 tranche en posant une ligne d'arbitrage.
 * [camp] résout la lacune UML documentée en tête de V1__schema_initial.sql (migration V4) :
 * NULL signale une ligne d'arbitrage CU20 (jamais une signature de camp DEUX_ZERO), non-NULL
 * signale une signature de camp. [signataire] est le capitaine/vice-capitaine du camp pour une
 * signature, ou l'organisateur du 2-0 pour une ligne d'arbitrage. */
@Entity
@Table(name = "validation")
class ValidationEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    val match: MatchEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    val camp: CampEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signataire_id", nullable = false)
    val signataire: UserEntity,

    @Column(nullable = false, columnDefinition = "text")
    val decision: String,

    @Column(columnDefinition = "text")
    val commentaire: String? = null,

    @Column(columnDefinition = "text")
    val correctionProposee: String? = null,

    val horodatage: Instant = Instant.now(),
)
