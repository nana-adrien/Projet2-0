package empire.digiprem.projet2_0.contracts.statistiques

import kotlinx.serialization.Serializable

// Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt).

/** CU24 — vote "homme du match". RG-16 : un seul vote par joueur et par match, jamais pour
 * soi-même, fenêtre de 24h, joueur présent sur la feuille de match requis (cf. VoteService). */
@Serializable
data class VoteRequest(
    val candidatId: String,
)

@Serializable
data class VoteDto(
    val id: String,
    val matchId: String,
    val votantId: String,
    val candidatId: String,
    val horodatage: String,
)

/** RG-16 — en cas d'égalité de voix, la distinction est partagée : [gagnants] peut contenir
 * plusieurs utilisateurs ex-aequo. */
@Serializable
data class VoteResultatDto(
    val gagnants: List<String>,
    val voix: List<ClassementEntryDto>,
    val fenetreOuverte: Boolean,
)
