package empire.digiprem.projet2_0.feature.calendrier.fiche_match

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp

/** E08 — Fiche du match et présences (CU15). */
data class FicheMatchState(
    val match: Match? = null,
    val deuxZeroId: String = "",
    val convocations: List<Convocation> = emptyList(),
    val camps: List<Camp> = emptyList(),
    val suisOrganisateur: Boolean = false,
    val currentUserId: String? = null,
    val isLoading: Boolean = true,
    val isActionLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
    val showModifierDialog: Boolean = false,
) {
    val maConvocation: Convocation? get() = convocations.firstOrNull { it.utilisateurId == currentUserId }

    /** Camp dont l'utilisateur courant est capitaine ou vice-capitaine (E09 — composition). */
    val monCamp: Camp?
        get() = camps.firstOrNull { it.capitaineId == currentUserId || it.viceCapitaineId == currentUserId }

    val estMarqueurOuSuppleant: Boolean
        get() = match?.marqueurId == currentUserId || match?.marqueurSuppleantId == currentUserId

    val peutSuivreEnDirect: Boolean
        get() = match?.statut in setOf("EN_COURS", "EN_ATTENTE_DE_VALIDATION", "OFFICIEL", "EN_LITIGE")

    val peutValiderResultat: Boolean
        get() = match?.statut == "EN_ATTENTE_DE_VALIDATION" && monCamp != null

    val peutArbitrerLitige: Boolean
        get() = match?.statut == "EN_LITIGE" && suisOrganisateur

    /** Fenêtre de 24h (RG-16) et éligibilité (présence sur la feuille) vérifiées côté serveur. */
    val peutVoterHommeDuMatch: Boolean
        get() = match?.statut == "OFFICIEL"

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
