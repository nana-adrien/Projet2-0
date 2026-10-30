package empire.digiprem.projet2_0.feature.deuxzero.create_deuxzero

import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZeroVisibilite

sealed interface CreateDeuxZeroAction {
    data class OnInit(val clubId: String) : CreateDeuxZeroAction
    data object OnNextStep : CreateDeuxZeroAction
    data object OnPreviousStep : CreateDeuxZeroAction

    data class OnNomChange(val value: String) : CreateDeuxZeroAction
    data class OnLieuChange(val value: String) : CreateDeuxZeroAction
    data class OnVisibiliteChange(val value: DeuxZeroVisibilite) : CreateDeuxZeroAction

    data class OnEstInterneChange(val value: Boolean) : CreateDeuxZeroAction
    data class OnDureeMiTempsChange(val value: Int) : CreateDeuxZeroAction
    data class OnNombreMiTempsChange(val value: Int) : CreateDeuxZeroAction
    data class OnNombreMaxJoueursChange(val value: Int) : CreateDeuxZeroAction
    data class OnDureePauseChange(val value: Int) : CreateDeuxZeroAction

    data class OnToggleJour(val jour: String) : CreateDeuxZeroAction
    data class OnHeureHabituelleChange(val value: String) : CreateDeuxZeroAction

    data class OnDelaiValidationChange(val value: Int) : CreateDeuxZeroAction
    data class OnTransfertsAutorisesChange(val value: Int?) : CreateDeuxZeroAction

    data class OnLibelleCampAChange(val value: String) : CreateDeuxZeroAction
    data class OnLibelleCampBChange(val value: String) : CreateDeuxZeroAction
    data class OnLibelleSaisonChange(val value: String) : CreateDeuxZeroAction
    data class OnDateDebutSaisonChange(val value: String) : CreateDeuxZeroAction

    data object OnSubmit : CreateDeuxZeroAction
    data object OnManageCampAEffectif : CreateDeuxZeroAction
    data object OnFinish : CreateDeuxZeroAction
}
