package empire.digiprem.projet2_0.feature.calendrier.fiche_match

import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.calendrier.model.StatutMatchModifiable

sealed interface FicheMatchAction {
    data class OnInit(val match: Match, val deuxZeroId: String) : FicheMatchAction
    data object OnRetry : FicheMatchAction
    data object OnGenererConvocationsClick : FicheMatchAction
    data object OnRelancerClick : FicheMatchAction
    data class OnRepondre(val reponse: ReponseConvocation) : FicheMatchAction
    data object OnShowModifierDialog : FicheMatchAction
    data object OnDismissModifierDialog : FicheMatchAction
    data class OnModifierConfirm(
        val nouveauStatut: StatutMatchModifiable,
        val nouvelleDate: String?,
        val nouvelleHeure: String?,
        val motif: String?,
    ) : FicheMatchAction
    data object OnMessageShown : FicheMatchAction
}
