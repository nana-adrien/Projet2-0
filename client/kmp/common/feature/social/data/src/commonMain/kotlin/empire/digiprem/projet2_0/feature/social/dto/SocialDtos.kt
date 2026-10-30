package empire.digiprem.projet2_0.feature.social.dto

import kotlinx.serialization.Serializable

/** Contrat REST provisoire (Jalon 6, pas de serveur dédié encore construit). */
@Serializable
data class SignalerRequest(
    val contenuType: String,
    val contenuId: String,
    val motif: String,
)

@Serializable
data class SignalementDto(
    val id: String,
    val contenuType: String,
    val contenuId: String,
    val motif: String,
)
