package empire.digiprem.projet2_0.feature.notifications.preferences

import empire.digiprem.projet2_0.feature.notifications.model.NiveauNotification

sealed interface PreferenceAction {
    data object OnInit : PreferenceAction
    data object OnRetry : PreferenceAction
    data class OnDeuxZeroChange(val deuxZeroId: String) : PreferenceAction
    data class OnNiveauChange(val niveau: NiveauNotification) : PreferenceAction
    data class OnNeRienDerangerChange(val value: Boolean) : PreferenceAction
    data object OnEnregistrerClick : PreferenceAction
    data object OnMessageShown : PreferenceAction
}
