package empire.digiprem.projet2_0.contracts.calendrier

import kotlinx.serialization.Serializable

/** Réponses possibles d'un joueur convoqué. SANS_REPONSE n'est jamais une valeur saisie par le
 * joueur — c'est l'état initial par défaut de [ConvocationDto.reponse] (voir ConvocationEntity). */
enum class ReponseConvocation { PRESENT, ABSENT, INCERTAIN }

// Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt).
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
