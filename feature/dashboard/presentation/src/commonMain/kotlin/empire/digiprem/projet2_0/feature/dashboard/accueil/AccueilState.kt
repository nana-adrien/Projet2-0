package empire.digiprem.projet2_0.feature.dashboard.accueil

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.Presences
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero

/**
 * E05 — Accueil (CU18, CU22). Pas de mini tableau de score de saison ni de carte "en direct" :
 * nécessiteraient respectivement de résoudre la saison courante (ListSaisonsUseCase) et un flux
 * CU18 temps réel, hors scope de cette passe — voir le rapport d'audit maquette. Le dernier
 * résultat et les présences par camp (point 3 de la maquette) sont en revanche câblés : les
 * données existent depuis les Jalons 3/5.
 */
data class AccueilState(
    val mesDeuxZero: List<DeuxZero> = emptyList(),
    val deuxZeroActuel: DeuxZero? = null,
    val prochainMatch: Match? = null,
    val dernierResultat: Match? = null,
    val convocations: List<Convocation> = emptyList(),
    val presences: Presences? = null,
    val currentUserId: String? = null,
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
    val showChangerDeuxZero: Boolean = false,
) {
    val maConvocation: Convocation? get() = convocations.firstOrNull { it.utilisateurId == currentUserId }
    val nombrePresents: Int get() = convocations.count { it.reponse == ReponseConvocation.PRESENT }
}
