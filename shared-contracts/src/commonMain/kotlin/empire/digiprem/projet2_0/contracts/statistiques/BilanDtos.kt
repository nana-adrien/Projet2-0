package empire.digiprem.projet2_0.contracts.statistiques

import kotlinx.serialization.Serializable

// Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt).

/** CU25 — bilan de clôture de saison. RG-17 : si l'égalité persiste jusqu'au dernier critère de
 * départage (résultat du dernier match officiel), [campChampionId] reste nul et
 * [egaliteTotale] vaut true plutôt que de forcer un gagnant arbitraire. */
@Serializable
data class BilanDto(
    val saisonId: String,
    val campChampionId: String?,
    val egaliteTotale: Boolean,
    val scoreCampA: Int,
    val scoreCampB: Int,
    val meilleurButeur: ClassementEntryDto?,
    val meilleurPasseur: ClassementEntryDto?,
    val plusAssidu: ClassementEntryDto?,
    val plusBeauMatchId: String?,
    val plusLongueSerie: Int,
)

/** CU25 — une ligne du palmarès d'un 2-0 (une par saison clôturée). */
@Serializable
data class PalmaresEntryDto(
    val saisonId: String,
    val libelle: String,
    val campChampionId: String?,
)
