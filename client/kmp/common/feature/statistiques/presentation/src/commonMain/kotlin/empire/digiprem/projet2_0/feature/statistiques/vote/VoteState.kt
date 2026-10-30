package empire.digiprem.projet2_0.feature.statistiques.vote

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenement
import empire.digiprem.projet2_0.feature.statistiques.model.Candidat
import empire.digiprem.projet2_0.feature.statistiques.model.VoteHommeDuMatch

/** E20 — Vote homme du match (CU24, RG-16). Détection du poste gardien non modélisée :
 * la ligne de stats se limite aux buts/passes comptés depuis la chronologie. */
data class VoteState(
    val matchId: String = "",
    val deuxZeroId: String = "",
    val campA: Camp? = null,
    val campB: Camp? = null,
    val currentUserId: String? = null,
    val vote: VoteHommeDuMatch? = null,
    val candidatSelectionneId: String? = null,
    val isLoading: Boolean = true,
    val isActionLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
) {
    fun statLigne(candidat: Candidat): String {
        val evenements = vote?.evenements.orEmpty().filterNot { it.annule }
        val buts = evenements.count { it.joueurId == candidat.utilisateurId && it.type == TypeEvenement.BUT }
        val passes = evenements.count { it.joueurSecondaireId == candidat.utilisateurId && it.type == TypeEvenement.BUT }
        return when {
            buts > 0 && passes > 0 -> "$buts but${if (buts > 1) "s" else ""}, $passes passe${if (passes > 1) "s" else ""}"
            buts > 0 -> "$buts but${if (buts > 1) "s" else ""}"
            passes > 0 -> "$passes passe${if (passes > 1) "s" else ""}"
            else -> "Présent"
        }
    }
}
