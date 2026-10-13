package empire.digiprem.projet2_0.contracts.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val pseudo: String?,
    val photo: String?,
    val nom: String?,
    val prenom: String?,
    val dateNaissance: String?,
    val postePrefere: String?,
    val piedFort: String?,
    val cacherNomReel: Boolean?,
    val cacherAge: Boolean?,
)

@Serializable
data class DeleteAccountRequest(
    val confirm: Boolean,
)
