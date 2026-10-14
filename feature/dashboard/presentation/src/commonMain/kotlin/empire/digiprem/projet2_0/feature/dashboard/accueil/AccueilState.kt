package empire.digiprem.projet2_0.feature.dashboard.accueil

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero

/**
 * E05 — Accueil, fortement simplifié : pas de tableau de score de saison (CU22, Jalon 5, aucune
 * donnée de classement n'existe encore) ni de carte "dernier résultat" (Match.scoreCampA/B ne sont
 * jamais renseignés avant la saisie de résultat, CU16/Jalon 3) ni de carte "en direct" (CU18,
 * Jalon 3). Ne reste que ce que les données actuelles permettent : sélecteur de 2-0 + prochain
 * match avec réponse à la convocation, fidèle au point 3 de la maquette.
 */
data class AccueilState(
    val mesDeuxZero: List<DeuxZero> = emptyList(),
    val deuxZeroActuel: DeuxZero? = null,
    val prochainMatch: Match? = null,
    val convocations: List<Convocation> = emptyList(),
    val currentUserId: String? = null,
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
    val showChangerDeuxZero: Boolean = false,
) {
    val maConvocation: Convocation? get() = convocations.firstOrNull { it.utilisateurId == currentUserId }
    val nombrePresents: Int get() = convocations.count { it.reponse == ReponseConvocation.PRESENT }
}
