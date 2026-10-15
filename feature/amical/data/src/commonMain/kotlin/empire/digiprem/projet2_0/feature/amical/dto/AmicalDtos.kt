package empire.digiprem.projet2_0.feature.amical.dto

import kotlinx.serialization.Serializable

/** Contrat REST provisoire (Jalon 6, pas de serveur dédié encore construit). */
@Serializable
data class CreerMatchAmicalRequest(
    val nomEquipeInvitee: String,
    val date: String,
    val heure: String,
    val dureeMiTempsMinutes: Int?,
)
