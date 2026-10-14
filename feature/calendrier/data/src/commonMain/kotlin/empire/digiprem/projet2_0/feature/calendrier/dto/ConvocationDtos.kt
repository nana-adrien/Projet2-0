package empire.digiprem.projet2_0.feature.calendrier.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConvocationDto(
    val id: String,
    val matchId: String,
    val utilisateurId: String,
    val pseudo: String,
    val reponse: String,
    val dateReponse: String?,
)

@Serializable
data class RepondreConvocationRequest(
    val reponse: String,
)

@Serializable
data class PresencesCampDto(
    val campId: String,
    val libelle: String,
    val presents: Int,
    val absents: Int,
    val incertains: Int,
    val sansReponse: Int,
)

@Serializable
data class PresencesDto(
    val matchId: String,
    val camps: List<PresencesCampDto>,
)
