package empire.digiprem.projet2_0.contracts.statistiques

import kotlinx.serialization.Serializable

// Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt).

/** CU24/CU25 (F-REC-02) — badge obtenu par un utilisateur. Seuls PREMIER_BUT, DOUBLE, TRIPLE,
 * QUADRUPLE et PASSEUR_DE_GENIE sont calculés par BadgeAttributionService ; FIDELE, MUR,
 * INVAINCU, HOMME_FORT, TOUJOURS_LA et PERSONNALISE ne le sont pas (limitation produit,
 * cf. KDoc BadgeAttributionService). */
@Serializable
data class BadgeObtenuDto(
    val id: String,
    val utilisateurId: String,
    val type: String,
    val contexte: String?,
    val date: String,
)
