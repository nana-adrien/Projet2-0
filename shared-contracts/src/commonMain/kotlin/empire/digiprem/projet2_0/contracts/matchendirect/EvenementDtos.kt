package empire.digiprem.projet2_0.contracts.matchendirect

import empire.digiprem.projet2_0.contracts.calendrier.MatchDto
import kotlinx.serialization.Serializable

/** Types saisissables via [AjouterEvenementRequest] (CU17). DEBUT_PERIODE/FIN_PERIODE sont gérés
 * par les endpoints dédiés démarrer/mi-temps/terminer, jamais saisis directement. */
enum class TypeEvenementSaisissable {
    BUT, BUT_CONTRE_SON_CAMP, PENALTY_MARQUE, PENALTY_MANQUE, ARRET_DECISIF, CARTON, REMPLACEMENT
}

@Serializable
data class EvenementDto(
    val id: String,
    val matchId: String,
    val type: String,
    val minute: Int,
    val periode: Int,
    val campId: String?,
    val joueurId: String?,
    val joueurPseudo: String?,
    val joueurSecondaireId: String?,
    val joueurSecondairePseudo: String?,
    val auteurSaisieId: String,
    val horodatage: String,
    val annule: Boolean,
)

@Serializable
data class AjouterEvenementRequest(
    val type: String,
    val minute: Int,
    val periode: Int,
    val campId: String?,
    val joueurId: String?,
    val joueurSecondaireId: String?,
    val clientEventId: String?,
)

@Serializable
data class MatchEnDirectDto(
    val match: MatchDto,
    val evenements: List<EvenementDto>,
)

@Serializable
data class PeriodeRequest(
    val periodeTerminee: Int,
)
