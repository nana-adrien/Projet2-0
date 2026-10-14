package empire.digiprem.projet2_0.feature.dashboard.accueil

import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero

sealed interface AccueilAction {
    data object OnLoad : AccueilAction
    data object OnRetry : AccueilAction
    data object OnChangerDeuxZeroClick : AccueilAction
    data object OnDismissChangerDeuxZero : AccueilAction
    data class OnSelectDeuxZero(val deuxZero: DeuxZero) : AccueilAction
    data class OnRepondre(val reponse: ReponseConvocation) : AccueilAction
    data class OnMatchClick(val match: Match) : AccueilAction
    data object OnMessageShown : AccueilAction
}
