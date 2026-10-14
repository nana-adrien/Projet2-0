package empire.digiprem.projet2_0.feature.deuxzero.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpenSaisonRequest(
    val libelle: String,
    val dateDebut: String,
    val dateFin: String?,
)

@Serializable
data class SaisonDto(
    val id: String,
    val deuxZeroId: String,
    val libelle: String,
    val dateDebut: String,
    val dateFin: String?,
    val statut: String,
    val campChampionId: String?,
)
