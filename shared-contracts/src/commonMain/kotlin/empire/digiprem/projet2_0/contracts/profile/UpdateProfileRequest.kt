package empire.digiprem.projet2_0.contracts.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val pseudo: String? = null,
    val photo: String? = null,
    val nom: String? = null,
    val prenom: String? = null,
    val dateNaissance: String? = null,
    val postePrefere: String? = null,
    val piedFort: String? = null,
    val cacherNomReel: Boolean? = null,
    val cacherAge: Boolean? = null,
)

@Serializable
data class DeleteAccountRequest(
    val confirm: Boolean,
)
