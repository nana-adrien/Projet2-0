package empire.digiprem.projet2_0.feature.calendrier.calendrier

import empire.digiprem.projet2_0.core.domain.util.todayIso
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.calendrier.model.Match

enum class CalendrierFiltre { A_VENIR, PASSES }

private const val STATUT_ANNULE = "ANNULE"

/** E07 — Calendrier des matchs d'un 2-0 (CU14). Simplifié à un seul 2-0 à la fois : il n'existe
 * pas encore d'endpoint "mes 2-0" permettant d'agréger tous les 2-0 du joueur comme le montre la
 * maquette — limitation documentée, pas un choix silencieux. */
data class CalendrierState(
    val deuxZeroId: String = "",
    val filtre: CalendrierFiltre = CalendrierFiltre.A_VENIR,
    val matchs: List<Match> = emptyList(),
    val isLoading: Boolean = true,
    val isGenerating: Boolean = false,
    val errorMessage: UiText? = null,
    val showAjouterMatchDialog: Boolean = false,
) {
    val matchsFiltres: List<Match>
        get() {
            val aujourdHui = todayIso()
            return matchs.filter { match ->
                val estPasse = match.date < aujourdHui || match.statut == STATUT_ANNULE
                if (filtre == CalendrierFiltre.A_VENIR) !estPasse else estPasse
            }
        }
}
