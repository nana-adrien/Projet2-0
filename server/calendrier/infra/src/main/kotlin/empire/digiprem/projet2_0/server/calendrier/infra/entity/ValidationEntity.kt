package empire.digiprem.projet2_0.server.calendrier.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

/** CU19 — un capitaine/vice-capitaine signe la feuille de match (VALIDE/CONTESTE) pour son camp.
 * CU20 — en cas de litige (CONTESTE d'au moins un camp, ou expiration du délai RG-12), l'organisateur
 * du 2-0 tranche en posant une ligne d'arbitrage.
 * [campId] résout la lacune UML documentée en tête de V1__schema_initial.sql (migration V4) :
 * NULL signale une ligne d'arbitrage CU20 (jamais une signature de camp DEUX_ZERO), non-NULL
 * signale une signature de camp. [signataireId] est le capitaine/vice-capitaine du camp pour une
 * signature, ou l'organisateur du 2-0 pour une ligne d'arbitrage. */
@Entity
@Table(name = "validation")
class ValidationEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    val matchId: UUID,

    val campId: UUID? = null,

    val signataireId: UUID,

    @Column(nullable = false, columnDefinition = "text")
    val decision: String,

    @Column(columnDefinition = "text")
    val commentaire: String? = null,

    @Column(columnDefinition = "text")
    val correctionProposee: String? = null,

    val horodatage: Instant = Instant.now(),
)
