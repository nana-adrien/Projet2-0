package empire.digiprem.projet2_0.feature.calendrier.dto

import kotlinx.serialization.Serializable

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
