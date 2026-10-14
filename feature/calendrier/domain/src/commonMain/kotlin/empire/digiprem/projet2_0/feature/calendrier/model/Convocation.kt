package empire.digiprem.projet2_0.feature.calendrier.model

/** SANS_REPONSE est l'état initial, jamais saisi par le joueur lui-même (cf. ConvocationEntity). */
enum class ReponseConvocation { PRESENT, ABSENT, INCERTAIN, SANS_REPONSE }

data class Convocation(
    val id: String,
    val matchId: String,
    val utilisateurId: String,
    val pseudo: String,
    val reponse: ReponseConvocation,
    val dateReponse: String?,
)

data class PresencesCamp(
    val campId: String,
    val libelle: String,
    val presents: Int,
    val absents: Int,
    val incertains: Int,
    val sansReponse: Int,
)

data class Presences(
    val matchId: String,
    val camps: List<PresencesCamp>,
)
