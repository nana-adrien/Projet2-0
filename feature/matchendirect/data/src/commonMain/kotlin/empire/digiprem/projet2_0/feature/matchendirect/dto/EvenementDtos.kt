package empire.digiprem.projet2_0.feature.matchendirect.dto

import empire.digiprem.projet2_0.feature.calendrier.dto.MatchDto
import kotlinx.serialization.Serializable

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
