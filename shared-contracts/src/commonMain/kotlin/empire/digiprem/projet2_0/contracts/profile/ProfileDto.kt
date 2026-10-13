package empire.digiprem.projet2_0.contracts.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val userId: String,
    val pseudo: String,
    val photo: String? = null,
    val nom: String? = null,
    val prenom: String? = null,
    val dateNaissance: String? = null,
    val postePrefere: String? = null,
    val piedFort: String? = null,
    val cacherNomReel: Boolean = false,
    val cacherAge: Boolean = false,
)
