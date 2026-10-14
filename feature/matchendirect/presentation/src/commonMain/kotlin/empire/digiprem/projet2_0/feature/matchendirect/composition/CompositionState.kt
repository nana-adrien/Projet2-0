package empire.digiprem.projet2_0.feature.matchendirect.composition

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.matchendirect.model.Composition

/** E09 — Composition et marqueur (CU16). Le marqueur/suppléant est choisi parmi l'effectif de CE
 * camp : la maquette permet "n'importe quel utilisateur" (y compris un simple spectateur), ce qui
 * nécessiterait une recherche globale d'utilisateurs qui n'existe pas encore — limitation
 * documentée, pas un choix silencieux. */
data class CompositionState(
    val camp: Camp? = null,
    val matchId: String = "",
    val effectif: List<Effectif> = emptyList(),
    val convocations: List<Convocation> = emptyList(),
    val composition: List<Composition> = emptyList(),
    val match: Match? = null,
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
    val showMarqueurPicker: Boolean = false,
    val showSuppleantPicker: Boolean = false,
) {
    val joueursEligibles: List<Effectif>
        get() = effectif.filter { joueur ->
            val reponse = convocations.firstOrNull { it.utilisateurId == joueur.utilisateurId }?.reponse
            reponse == ReponseConvocation.PRESENT || reponse == ReponseConvocation.INCERTAIN
        }

    fun compositionDe(utilisateurId: String): Composition? = composition.firstOrNull { it.utilisateurId == utilisateurId }

    val titulaires: List<Effectif> get() = joueursEligibles.filter { compositionDe(it.utilisateurId)?.titulaire == true }
    val remplacants: List<Effectif> get() = joueursEligibles.filter { compositionDe(it.utilisateurId)?.let { c -> !c.titulaire } == true }

    val marqueurPseudo: String? get() = effectif.firstOrNull { it.utilisateurId == match?.marqueurId }?.pseudo
    val suppleantPseudo: String? get() = effectif.firstOrNull { it.utilisateurId == match?.marqueurSuppleantId }?.pseudo
}
