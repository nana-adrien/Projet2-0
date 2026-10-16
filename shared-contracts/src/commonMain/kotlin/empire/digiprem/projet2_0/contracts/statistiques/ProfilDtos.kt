package empire.digiprem.projet2_0.contracts.statistiques

import kotlinx.serialization.Serializable

// Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt).

/** CU23 (F-PRO-04/06) — profil public d'un joueur. [nom]/[prenom]/[dateNaissance] sont masqués
 * côté serveur (ProfilProgressionService) selon les préférences de confidentialité de
 * l'utilisateur consulté, si le viewer n'est pas l'utilisateur lui-même. */
@Serializable
data class ProfilPublicDto(
    val utilisateurId: String,
    val pseudo: String,
    val nom: String?,
    val prenom: String?,
    val photo: String?,
    val dateNaissance: String?,
    val statistiques: StatistiquesUtilisateurDto,
)

/** CU23 — un point de la courbe de progression. [periode] = "yyyy-MM" en granularité MOIS,
 * ou le libellé de la saison en granularité SAISON. */
@Serializable
data class PointProgressionDto(
    val periode: String,
    val buts: Int,
    val presences: Int,
)

@Serializable
data class ProgressionMensuelleDto(
    val points: List<PointProgressionDto>,
)
