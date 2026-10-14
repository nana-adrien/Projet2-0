package empire.digiprem.projet2_0.feature.calendrier.fiche_match

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation

/** E08 — Fiche du match et présences (CU15). */
data class FicheMatchState(
    val match: Match? = null,
    val convocations: List<Convocation> = emptyList(),
    val currentUserId: String? = null,
    val isLoading: Boolean = true,
    val isActionLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
    val showModifierDialog: Boolean = false,
) {
    val maConvocation: Convocation? get() = convocations.firstOrNull { it.utilisateurId == currentUserId }

    val convocationsTriees: List<Convocation>
        get() = convocations.sortedBy {
            when (it.reponse) {
                ReponseConvocation.PRESENT      -> 0
                ReponseConvocation.INCERTAIN    -> 1
                ReponseConvocation.SANS_REPONSE -> 2
                ReponseConvocation.ABSENT       -> 3
            }
        }

    val nombrePresents: Int get() = convocations.count { it.reponse == ReponseConvocation.PRESENT }
}
