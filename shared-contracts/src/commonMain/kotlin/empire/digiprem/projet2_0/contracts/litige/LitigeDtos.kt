package empire.digiprem.projet2_0.contracts.litige

import empire.digiprem.projet2_0.contracts.calendrier.MatchDto
import empire.digiprem.projet2_0.contracts.matchendirect.EvenementDto
import kotlinx.serialization.Serializable

/** CU19/CU20 (Jalon 4) — validation du résultat et arbitrage de litige.
 * Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt) : encodeDefaults = false
 * côté serveur ferait silencieusement disparaître du JSON tout champ égal à sa valeur par défaut. */
@Serializable
data class ValidationDto(
    val id: String,
    val matchId: String,
    val campId: String,
    val signataireId: String,
    val signatairePseudo: String,
    val decision: String,
    val commentaire: String?,
    val correctionProposee: String?,
    val horodatage: String,
)

@Serializable
data class ResultatMatchDto(
    val match: MatchDto,
    val evenements: List<EvenementDto>,
    val validations: List<ValidationDto>,
)

@Serializable
data class ContesterRequest(
    val correctionProposee: String,
    val commentaire: String?,
)

@Serializable
data class RepondreContestationRequest(
    val accepter: Boolean,
    val commentaire: String?,
)

@Serializable
data class TrancherLitigeRequest(
    val decision: String,
    val motif: String,
)
