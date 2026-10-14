package empire.digiprem.projet2_0.feature.validation.dto

import empire.digiprem.projet2_0.feature.calendrier.dto.MatchDto
import empire.digiprem.projet2_0.feature.matchendirect.dto.EvenementDto
import kotlinx.serialization.Serializable

/**
 * Contrat REST en attente de confirmation côté serveur (Jalon 4 en cours de construction en
 * parallèle) : routes et formes de payload déduites des conventions déjà en place pour
 * CU16-CU18 (feature:matchendirect) et des fiches UML CU19/CU20. À ajuster dès que le serveur
 * est disponible — c'est précisément ce que l'architecture en couches isole à cette seule classe.
 */
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
