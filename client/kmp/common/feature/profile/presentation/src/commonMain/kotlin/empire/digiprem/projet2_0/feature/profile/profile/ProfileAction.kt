package empire.digiprem.projet2_0.feature.profile.profile

import empire.digiprem.projet2_0.core.domain.model.PiedFort
import empire.digiprem.projet2_0.core.domain.model.Poste

sealed interface ProfileAction {
    data object OnRetry                                      : ProfileAction
    data class OnPseudoChange(val value: String)             : ProfileAction
    data class OnNomChange(val value: String)                : ProfileAction
    data class OnPrenomChange(val value: String)              : ProfileAction
    data class OnDateNaissanceChange(val value: String)       : ProfileAction
    data class OnPosteChange(val value: Poste)                : ProfileAction
    data class OnPiedFortChange(val value: PiedFort)           : ProfileAction
    data class OnCacherNomReelChange(val value: Boolean)       : ProfileAction
    data class OnCacherAgeChange(val value: Boolean)           : ProfileAction
    data object OnSaveClick                                  : ProfileAction
    data object OnDeleteAccountClick                          : ProfileAction
    data object OnConfirmDeleteAccount                        : ProfileAction
    data object OnDismissDeleteDialog                         : ProfileAction
}
