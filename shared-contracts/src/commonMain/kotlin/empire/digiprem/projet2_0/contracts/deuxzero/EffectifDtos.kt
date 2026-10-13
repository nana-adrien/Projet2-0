package empire.digiprem.projet2_0.contracts.deuxzero

import kotlinx.serialization.Serializable

@Serializable
data class AddEffectifRequest(
    val utilisateurId: String,
    val numeroMaillot: Int?,
)

@Serializable
data class EffectifDto(
    val id: String,
    val campId: String,
    val utilisateurId: String,
    val pseudo: String,
    val numeroMaillot: Int?,
    val dateEntree: String,
    val dateSortie: String?,
)

@Serializable
data class TransferPlayerRequest(
    val utilisateurId: String,
    val nouveauCampId: String,
)
