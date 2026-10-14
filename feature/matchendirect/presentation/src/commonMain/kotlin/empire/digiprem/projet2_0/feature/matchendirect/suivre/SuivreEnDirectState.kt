package empire.digiprem.projet2_0.feature.matchendirect.suivre

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.matchendirect.model.Composition
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenement

enum class SuivreEnDirectTab { RESUME, COMPOSITIONS }

/** E12 — Suivre le match en direct (CU18), lecture seule via WebSocket. */
data class SuivreEnDirectState(
    val matchId: String = "",
    val deuxZeroId: String = "",
    val match: Match? = null,
    val evenements: List<Evenement> = emptyList(),
    val campA: Camp? = null,
    val campB: Camp? = null,
    val effectifA: List<Effectif> = emptyList(),
    val effectifB: List<Effectif> = emptyList(),
    val compositions: List<Composition> = emptyList(),
    val tab: SuivreEnDirectTab = SuivreEnDirectTab.RESUME,
    val idEvenementSurligne: String? = null,
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
) {
    val scoreCampA: Int get() = match?.scoreCampA ?: 0
    val scoreCampB: Int get() = match?.scoreCampB ?: 0

    val minuteActuelle: Int
        get() = evenements.filterNot { it.annule }.maxOfOrNull { it.minute } ?: 0

    val chronologie: List<Evenement>
        get() = evenements
            .filterNot { it.annule }
            .filter { it.type != TypeEvenement.DEBUT_PERIODE && it.type != TypeEvenement.FIN_PERIODE }
            .sortedByDescending { it.minute }

    fun titulairesDuCamp(camp: Camp?): List<Effectif> = effectifDuCamp(camp, titulaire = true)
    fun remplacantsDuCamp(camp: Camp?): List<Effectif> = effectifDuCamp(camp, titulaire = false)

    private fun effectifDuCamp(camp: Camp?, titulaire: Boolean): List<Effectif> {
        if (camp == null) return emptyList()
        val effectif = if (camp.id == campA?.id) effectifA else effectifB
        val ids = compositions.filter { it.titulaire == titulaire }.map { it.utilisateurId }.toSet()
        return effectif.filter { it.utilisateurId in ids }
    }
}
