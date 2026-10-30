package empire.digiprem.projet2_0.feature.social.signaler

import empire.digiprem.projet2_0.feature.social.model.TypeContenuSignale

sealed interface SignalerAction {
    data class OnInit(val contenuType: TypeContenuSignale, val contenuId: String) : SignalerAction
    data class OnMotifChange(val value: String) : SignalerAction
    data object OnEnvoyerClick : SignalerAction
}
