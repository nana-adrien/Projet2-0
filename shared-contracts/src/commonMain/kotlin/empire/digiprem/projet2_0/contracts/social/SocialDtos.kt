package empire.digiprem.projet2_0.contracts.social

import kotlinx.serialization.Serializable

/**
 * CU27 — Signaler un contenu (F-SOC-05). Résout la lacune UML "Signalement sans référence au
 * contenu signalé" (cf. en-tête de V1__schema_initial.sql, colonnes ajoutées en V3 Jalon 6).
 * Seule la valeur PROFIL_UTILISATEUR est utilisée par le mobile à ce stade (bouton "Signaler ce
 * profil" sur E19) ; les autres natures de contenu ne sont pas modélisées par un CU dédié.
 */
enum class TypeContenuSignale { PROFIL_UTILISATEUR }

// Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt).
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
