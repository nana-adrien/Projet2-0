package empire.digiprem.projet2_0.feature.matchendirect.dto

import kotlinx.serialization.Serializable

// Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt).
@Serializable
data class CompositionDto(
    val id: String,
    val matchId: String,
    val utilisateurId: String,
    val pseudo: String,
    val titulaire: Boolean,
    val present: Boolean,
)

@Serializable
data class DesignerCompositionRequest(
    val utilisateurId: String,
    val titulaire: Boolean,
    val present: Boolean,
)

@Serializable
data class DesignerMarqueurRequest(
    val utilisateurId: String,
    val suppleant: Boolean,
)
