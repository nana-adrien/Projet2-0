package empire.digiprem.projet2_0.feature.deuxzero.parametres

import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZeroVisibilite
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif

sealed interface ParametresDeuxZeroAction {
    data class OnInit(val deuxZeroId: String) : ParametresDeuxZeroAction
    data object OnRetry : ParametresDeuxZeroAction
    data class OnToggleJour(val jour: String) : ParametresDeuxZeroAction
    data class OnHeureHabituelleChange(val value: String) : ParametresDeuxZeroAction
    data class OnDureeMiTempsChange(val value: String) : ParametresDeuxZeroAction
    data class OnDelaiValidationChange(val value: String) : ParametresDeuxZeroAction
    data class OnTransfertsAutorisesChange(val value: String) : ParametresDeuxZeroAction
    data class OnVisibiliteChange(val value: DeuxZeroVisibilite) : ParametresDeuxZeroAction
    data object OnSaveClick : ParametresDeuxZeroAction
    data object OnToggleArchiveClick : ParametresDeuxZeroAction
    data class OnTransferOrganisateurClick(val membre: Effectif) : ParametresDeuxZeroAction
    data object OnConfirmTransferOrganisateur : ParametresDeuxZeroAction
    data object OnDismissTransferOrganisateurDialog : ParametresDeuxZeroAction
    data object OnMessageShown : ParametresDeuxZeroAction
}
