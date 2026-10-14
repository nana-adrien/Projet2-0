package empire.digiprem.projet2_0.feature.matchendirect.marqueur

import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenementSaisissable

sealed interface MarqueurAction {
    data class OnInit(val matchId: String, val deuxZeroId: String) : MarqueurAction
    data object OnRetry : MarqueurAction
    data object OnDemarrerMatchClick : MarqueurAction
    data class OnGoalButtonClick(val camp: Camp) : MarqueurAction
    data object OnAutreClick : MarqueurAction
    data class OnAutreTypeClick(val camp: Camp, val type: TypeEvenementSaisissable, val titre: String) : MarqueurAction
    data class OnRemplacementClick(val camp: Camp) : MarqueurAction
    data object OnPasserMiTempsClick : MarqueurAction
    data object OnAnnulerDernierClick : MarqueurAction
    data object OnFinDuMatchClick : MarqueurAction
    data object OnConfirmerFinDuMatch : MarqueurAction
    data object OnDismissConfirmerFin : MarqueurAction
    data object OnDismissSheet : MarqueurAction
    data class OnEnregistrerBut(val buteurId: String?, val passeurId: String?, val contreSonCamp: Boolean) : MarqueurAction
    data class OnEnregistrerJoueurUnique(val joueurId: String) : MarqueurAction
    data class OnEnregistrerRemplacement(val sortantId: String, val entrantId: String) : MarqueurAction
    data object OnMessageShown : MarqueurAction
}
