package empire.digiprem.projet2_0.contracts.calendrier

import kotlinx.serialization.Serializable

/** Statuts autorisés en écriture via [ModifierMatchRequest] au Jalon 2 (CU14).
 * EN_COURS, EN_ATTENTE_DE_VALIDATION, OFFICIEL, EN_LITIGE existent en base (voir check SQL sur
 * `match.statut`) mais sont pilotés par la saisie de résultat (CU16, Jalon 3) — hors scope ici. */
enum class StatutMatchModifiable { PROGRAMME, REPORTE, ANNULE }

// Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt).
@Serializable
data class MatchDto(
    val id: String,
    val saisonId: String?,
    val type: String,
    val date: String,
    val heure: String,
    val lieu: String?,
    val statut: String,
    val scoreCampA: Int?,
    val scoreCampB: Int?,
    val dureeMiTempsAppliqueeMinutes: Int?,
    val nomEquipeInvitee: String?,
    val marqueurId: String?,
    val marqueurSuppleantId: String?,
)

@Serializable
data class AjouterMatchExceptionnelRequest(
    val date: String,
    val heure: String,
    val lieu: String?,
)

@Serializable
data class ModifierMatchRequest(
    val nouveauStatut: String,
    val nouvelleDate: String?,
    val nouvelleHeure: String?,
    val motif: String?,
    val dureeMiTempsAppliqueeMinutes: Int?,
)
