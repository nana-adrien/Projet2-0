package empire.digiprem.projet2_0.contracts.profile

import kotlinx.serialization.Serializable

// Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt) : chaque appelant passe
// des valeurs explicites, sans quoi Jackson sérialise ces propriétés en objet vide côté serveur.
@Serializable
data class ProfileDto(
    val userId: String,
    val pseudo: String,
    val photo: String?,
    val nom: String?,
    val prenom: String?,
    val dateNaissance: String?,
    val postePrefere: String?,
    val piedFort: String?,
    val cacherNomReel: Boolean,
    val cacherAge: Boolean,
)
