package empire.digiprem.projet2_0.feature.deuxzero.composer_effectif

import empire.digiprem.projet2_0.feature.deuxzero.model.Camp

sealed interface ComposerEffectifAction {
    data class OnInit(val camp: Camp, val clubId: String) : ComposerEffectifAction
    data object OnRetry : ComposerEffectifAction
    data class OnAddMember(val utilisateurId: String) : ComposerEffectifAction
    data class OnNameCaptain(val utilisateurId: String, val viceCapitaine: Boolean) : ComposerEffectifAction
    data object OnMessageShown : ComposerEffectifAction
    data object OnDoneClick : ComposerEffectifAction
}
