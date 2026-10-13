package empire.digiprem.projet2_0.contracts.deuxzero

import kotlinx.serialization.Serializable

// Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt).
@Serializable
data class CreateDeuxZeroRequest(
    val nom: String,
    val lieu: String?,
    val visibilite: String,
    val estInterne: Boolean,
    val joursHabituels: List<String>,
    val heureHabituelle: String?,
    val dureeMiTempsMinutes: Int,
    val nombreMiTemps: Int,
    val dureePauseMinutes: Int?,
    val nombreMaxJoueursParCamp: Int,
    val delaiValidationAutomatiqueHeures: Int,
    val transfertsAutorisesParSaison: Int?,
    val libelleCampA: String,
    val libelleCampB: String,
    val libelleSaison: String,
    val dateDebutSaison: String,
)

@Serializable
data class DeuxZeroDto(
    val id: String,
    val nom: String,
    val lieu: String?,
    val visibilite: String,
    val estInterne: Boolean,
    val joursHabituels: List<String>,
    val heureHabituelle: String?,
    val dureeMiTempsMinutes: Int,
    val nombreMiTemps: Int,
    val dureePauseMinutes: Int?,
    val nombreMaxJoueursParCamp: Int,
    val delaiValidationAutomatiqueHeures: Int,
    val transfertsAutorisesParSaison: Int?,
    val statut: String,
    val organisateurId: String,
)

@Serializable
data class UpdateDeuxZeroSettingsRequest(
    val dureeMiTempsMinutes: Int?,
    val joursHabituels: List<String>?,
    val heureHabituelle: String?,
    val delaiValidationAutomatiqueHeures: Int?,
    val transfertsAutorisesParSaison: Int?,
    val visibilite: String?,
    val statut: String?,
)

@Serializable
data class EngageClubRequest(val codeEngagement: String)

@Serializable
data class CampDto(
    val id: String,
    val deuxZeroId: String,
    val clubId: String?,
    val clubNom: String?,
    val libelle: String,
    val codeEngagement: String,
    val capitaineId: String?,
    val capitainePseudo: String?,
    val viceCapitaineId: String?,
    val viceCapitainePseudo: String?,
)

@Serializable
data class NommerCapitaineRequest(val utilisateurId: String, val viceCapitaine: Boolean)
