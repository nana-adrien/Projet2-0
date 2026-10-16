package empire.digiprem.projet2_0.server.calendrier.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

/** RG : un match de type DEUX_ZERO référence obligatoirement [saisonId] (Jalon 2, CU14/CU15).
 * Le type AMICAL (hors scope Jalon 2, cf. CU21 Jalon 6) référence [clubOrganisateurId] à la place.
 * [marqueurId] est hors scope Jalon 2 (utilisé à partir du Jalon 3, CU16). */
@Entity
@Table(name = "match")
class MatchEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    val saisonId: UUID? = null,

    val clubOrganisateurId: UUID? = null,

    @Column(nullable = false, columnDefinition = "text")
    val type: String,

    @Column(nullable = false)
    val date: LocalDate,

    @Column(nullable = false)
    val heure: LocalTime,

    @Column(columnDefinition = "text")
    val lieu: String? = null,

    @Column(nullable = false, columnDefinition = "text")
    val statut: String = "PROGRAMME",

    val scoreCampA: Int? = null,

    val scoreCampB: Int? = null,

    val dureeMiTempsAppliqueeMinutes: Int? = null,

    @Column(columnDefinition = "text")
    val nomEquipeInvitee: String? = null,

    val marqueurId: UUID? = null,

    /** CU16 — suppléant du marqueur (F-LIV-08 : reprend la saisie en cas de perte du téléphone). */
    val marqueurSuppleantId: UUID? = null,

    /** CU19/CU20 (Jalon 4) — horodatage du passage à EN_ATTENTE_DE_VALIDATION, posé par
     * EvenementService.terminerMatch. Nécessaire pour calculer l'échéance RG-12 (= cette date +
     * deuxZero.delaiValidationAutomatiqueHeures). */
    val entreEnAttenteValidationA: Instant? = null,
)
