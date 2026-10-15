package empire.digiprem.projet2_0.feature.deuxzero.parametres

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.getOrNull
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.UpdateDeuxZeroSettingsInput
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListEffectifUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListMesDeuxZeroUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.TransferOrganisateurUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.UpdateDeuxZeroSettingsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val STATUT_ARCHIVE = "ARCHIVE"
private const val STATUT_ACTIF = "ACTIF"

/** CU13 — Modifier les paramètres d'un 2-0. */
class ParametresDeuxZeroViewModel(
    private val listMesDeuxZeroUseCase: ListMesDeuxZeroUseCase,
    private val listCampsUseCase: ListCampsUseCase,
    private val listEffectifUseCase: ListEffectifUseCase,
    private val updateDeuxZeroSettingsUseCase: UpdateDeuxZeroSettingsUseCase,
    private val transferOrganisateurUseCase: TransferOrganisateurUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(ParametresDeuxZeroState())
    val state = _state.asStateFlow()

    private val _events = Channel<ParametresDeuxZeroEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: ParametresDeuxZeroAction) {
        when (action) {
            is ParametresDeuxZeroAction.OnInit -> init(action.deuxZeroId)
            ParametresDeuxZeroAction.OnRetry -> load()
            is ParametresDeuxZeroAction.OnToggleJour -> _state.update {
                val jours = if (action.jour in it.joursHabituels) it.joursHabituels - action.jour else it.joursHabituels + action.jour
                it.copy(joursHabituels = jours)
            }
            is ParametresDeuxZeroAction.OnHeureHabituelleChange -> _state.update { it.copy(heureHabituelle = action.value) }
            is ParametresDeuxZeroAction.OnDureeMiTempsChange -> _state.update { it.copy(dureeMiTempsMinutes = action.value) }
            is ParametresDeuxZeroAction.OnDelaiValidationChange -> _state.update { it.copy(delaiValidationAutomatiqueHeures = action.value) }
            is ParametresDeuxZeroAction.OnTransfertsAutorisesChange -> _state.update { it.copy(transfertsAutorisesParSaison = action.value) }
            is ParametresDeuxZeroAction.OnVisibiliteChange -> _state.update { it.copy(visibilite = action.value) }
            ParametresDeuxZeroAction.OnSaveClick -> save()
            ParametresDeuxZeroAction.OnToggleArchiveClick -> toggleArchive()
            is ParametresDeuxZeroAction.OnTransferOrganisateurClick ->
                _state.update { it.copy(showTransferOrganisateurDialog = true, targetOrganisateur = action.membre) }
            ParametresDeuxZeroAction.OnDismissTransferOrganisateurDialog ->
                _state.update { it.copy(showTransferOrganisateurDialog = false, targetOrganisateur = null) }
            ParametresDeuxZeroAction.OnConfirmTransferOrganisateur -> transferOrganisateur()
            ParametresDeuxZeroAction.OnMessageShown -> _state.update { it.copy(message = null) }
        }
    }

    private fun init(deuxZeroId: String) {
        _state.update { it.copy(deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        load()
    }

    private fun load() {
        val deuxZeroId = _state.value.deuxZeroId
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            listMesDeuxZeroUseCase()
                .onSuccess { deuxZeros ->
                    val deuxZero = deuxZeros.orEmpty().firstOrNull { it.id == deuxZeroId }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            statutActuel = deuxZero?.statut.orEmpty(),
                            joursHabituels = deuxZero?.joursHabituels?.toSet() ?: it.joursHabituels,
                            heureHabituelle = deuxZero?.heureHabituelle ?: it.heureHabituelle,
                            dureeMiTempsMinutes = deuxZero?.dureeMiTempsMinutes?.toString() ?: it.dureeMiTempsMinutes,
                            delaiValidationAutomatiqueHeures = deuxZero?.delaiValidationAutomatiqueHeures?.toString() ?: it.delaiValidationAutomatiqueHeures,
                            transfertsAutorisesParSaison = deuxZero?.transfertsAutorisesParSaison?.toString().orEmpty(),
                            visibilite = deuxZero?.visibilite ?: it.visibilite,
                        )
                    }
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }

            val camps = listCampsUseCase(deuxZeroId).getOrNull().orEmpty()
            val membres = camps.flatMap { camp -> listEffectifUseCase(camp.id).getOrNull().orEmpty() }
            _state.update { it.copy(membres = membres) }
        }
    }

    private fun save() {
        val s = _state.value
        if (!s.isFormValid) return
        _state.update { it.copy(isSaving = true, errorMessage = null) }
        viewModelScope.launch {
            updateDeuxZeroSettingsUseCase(
                s.deuxZeroId,
                UpdateDeuxZeroSettingsInput(
                    dureeMiTempsMinutes = s.dureeMiTempsMinutes.toIntOrNull(),
                    joursHabituels = s.joursHabituels.toList(),
                    heureHabituelle = s.heureHabituelle.ifBlank { null },
                    delaiValidationAutomatiqueHeures = s.delaiValidationAutomatiqueHeures.toIntOrNull(),
                    transfertsAutorisesParSaison = s.transfertsAutorisesParSaison.toIntOrNull(),
                    visibilite = s.visibilite,
                ),
            )
                .onSuccess {
                    _state.update { it.copy(isSaving = false, message = UiText.DynamicString("Paramètres enregistrés. Les matchs déjà programmés ne sont pas modifiés.")) }
                }
                .onFailure { error -> _state.update { it.copy(isSaving = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun toggleArchive() {
        val s = _state.value
        val nouveauStatut = if (s.estArchive) STATUT_ACTIF else STATUT_ARCHIVE
        _state.update { it.copy(isSaving = true, errorMessage = null) }
        viewModelScope.launch {
            updateDeuxZeroSettingsUseCase(s.deuxZeroId, UpdateDeuxZeroSettingsInput(statut = nouveauStatut))
                .onSuccess { deuxZero ->
                    _state.update {
                        it.copy(
                            isSaving = false,
                            statutActuel = deuxZero?.statut ?: nouveauStatut,
                            message = if (nouveauStatut == STATUT_ARCHIVE) UiText.DynamicString("2-0 archivé.") else UiText.DynamicString("2-0 réactivé."),
                        )
                    }
                }
                .onFailure { error -> _state.update { it.copy(isSaving = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun transferOrganisateur() {
        val s = _state.value
        val target = s.targetOrganisateur ?: return
        _state.update { it.copy(isTransferringOrganisateur = true, showTransferOrganisateurDialog = false) }
        viewModelScope.launch {
            transferOrganisateurUseCase(s.deuxZeroId, target.utilisateurId)
                .onSuccess {
                    _state.update { it.copy(isTransferringOrganisateur = false, targetOrganisateur = null) }
                    _events.send(ParametresDeuxZeroEvent.OnOrganisateurTransfere)
                }
                .onFailure { error -> _state.update { it.copy(isTransferringOrganisateur = false, errorMessage = error.toUiText()) } }
        }
    }
}
