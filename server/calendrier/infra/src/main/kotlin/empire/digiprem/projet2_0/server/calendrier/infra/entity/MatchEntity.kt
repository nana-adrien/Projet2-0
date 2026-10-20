package empire.digiprem.projet2_0.server.calendrier.infra.entity

import empire.digiprem.projet2_0.server.clubs.infra.entity.ClubEntity
import empire.digiprem.projet2_0.server.deuxzero.infra.entity.SaisonEntity
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
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

/** RG : un match de type DEUX_ZERO référence obligatoirement [saison] (Jalon 2, CU14/CU15).
 * Le type AMICAL (hors scope Jalon 2, cf. CU21 Jalon 6) référence [clubOrganisateur] à la place.
 * [marqueur] est hors scope Jalon 2 (utilisé à partir du Jalon 3, CU16). */
@Entity
@Table(name = "match")
class MatchEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saison_id")
    val saison: SaisonEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_organisateur_id")
    val clubOrganisateur: ClubEntity? = null,

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

    @Column(name = "score_camp_a")
    val scoreCampA: Int? = null,

    @Column(name = "score_camp_b")
    val scoreCampB: Int? = null,

    val dureeMiTempsAppliqueeMinutes: Int? = null,

    @Column(columnDefinition = "text")
    val nomEquipeInvitee: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marqueur_id")
    val marqueur: UserEntity? = null,

    /** CU16 — suppléant du marqueur (F-LIV-08 : reprend la saisie en cas de perte du téléphone). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marqueur_suppleant_id")
    val marqueurSuppleant: UserEntity? = null,

    /** CU19/CU20 (Jalon 4) — horodatage du passage à EN_ATTENTE_DE_VALIDATION, posé par
     * EvenementService.terminerMatch. Nécessaire pour calculer l'échéance RG-12 (= cette date +
     * deuxZero.delaiValidationAutomatiqueHeures). */
    @Column(name = "entre_en_attente_validation_a")
    val entreEnAttenteValidationA: Instant? = null,
)
