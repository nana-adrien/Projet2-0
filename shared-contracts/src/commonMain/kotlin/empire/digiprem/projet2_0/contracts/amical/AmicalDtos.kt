package empire.digiprem.projet2_0.contracts.amical

import kotlinx.serialization.Serializable

/**
 * CU21 — Organiser un match amical (F-AMI-01, F-AMI-03, RG-14, RG-15).
 * Scope serveur limité au scénario nominal mobile (E26) : adversaire "équipe invitée" uniquement.
 * Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt — encodeDefaults = false
 * côté serveur omettrait silencieusement tout champ égal à sa valeur par défaut Kotlin).
 */
@Serializable
data class CreerMatchAmicalRequest(
    val nomEquipeInvitee: String,
    val date: String,
    val heure: String,
    val dureeMiTempsMinutes: Int?,
)
