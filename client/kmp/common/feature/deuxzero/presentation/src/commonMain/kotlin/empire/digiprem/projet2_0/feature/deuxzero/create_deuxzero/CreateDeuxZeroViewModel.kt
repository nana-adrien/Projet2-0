package empire.digiprem.projet2_0.feature.deuxzero.create_deuxzero

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.CreateDeuxZeroInput
import empire.digiprem.projet2_0.feature.deuxzero.usecase.CreateDeuxZeroUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.EngageClubUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val STEP_COUNT = 5

class CreateDeuxZeroViewModel(
    private val createDeuxZeroUseCase: CreateDeuxZeroUseCase,
    private val listCampsUseCase: ListCampsUseCase,
    private val engageClubUseCase: EngageClubUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(CreateDeuxZeroState())
    val state = _state.asStateFlow()

    private val _events = Channel<CreateDeuxZeroEvent>()
    val events = _events.receiveAsFlow()

    private lateinit var clubId: String

    fun onAction(action: CreateDeuxZeroAction) {
        when (action) {
            is CreateDeuxZeroAction.OnInit -> clubId = action.clubId
            CreateDeuxZeroAction.OnNextStep -> _state.update { it.copy(step = (it.step + 1).coerceAtMost(STEP_COUNT)) }
            CreateDeuxZeroAction.OnPreviousStep -> _state.update { it.copy(step = (it.step - 1).coerceAtLeast(1)) }

            is CreateDeuxZeroAction.OnNomChange -> _state.update { it.copy(nom = action.value) }
            is CreateDeuxZeroAction.OnLieuChange -> _state.update { it.copy(lieu = action.value) }
            is CreateDeuxZeroAction.OnVisibiliteChange -> _state.update { it.copy(visibilite = action.value) }

            is CreateDeuxZeroAction.OnEstInterneChange -> _state.update { it.copy(estInterne = action.value) }
            is CreateDeuxZeroAction.OnDureeMiTempsChange -> _state.update { it.copy(dureeMiTempsMinutes = action.value) }
            is CreateDeuxZeroAction.OnNombreMiTempsChange -> _state.update { it.copy(nombreMiTemps = action.value) }
            is CreateDeuxZeroAction.OnNombreMaxJoueursChange -> _state.update { it.copy(nombreMaxJoueursParCamp = action.value) }
            is CreateDeuxZeroAction.OnDureePauseChange -> _state.update { it.copy(dureePauseMinutes = action.value) }

            is CreateDeuxZeroAction.OnToggleJour -> _state.update {
                val jours = if (action.jour in it.joursHabituels) it.joursHabituels - action.jour else it.joursHabituels + action.jour
                it.copy(joursHabituels = jours)
            }
            is CreateDeuxZeroAction.OnHeureHabituelleChange -> _state.update { it.copy(heureHabituelle = action.value) }

            is CreateDeuxZeroAction.OnDelaiValidationChange -> _state.update { it.copy(delaiValidationAutomatiqueHeures = action.value) }
            is CreateDeuxZeroAction.OnTransfertsAutorisesChange -> _state.update { it.copy(transfertsAutorisesParSaison = action.value) }

            is CreateDeuxZeroAction.OnLibelleCampAChange -> _state.update { it.copy(libelleCampA = action.value) }
            is CreateDeuxZeroAction.OnLibelleCampBChange -> _state.update { it.copy(libelleCampB = action.value) }
            is CreateDeuxZeroAction.OnLibelleSaisonChange -> _state.update { it.copy(libelleSaison = action.value) }
            is CreateDeuxZeroAction.OnDateDebutSaisonChange -> _state.update { it.copy(dateDebutSaison = action.value) }

            CreateDeuxZeroAction.OnSubmit -> submit()
            CreateDeuxZeroAction.OnManageCampAEffectif -> manageCampAEffectif()
            CreateDeuxZeroAction.OnFinish -> viewModelScope.launch { _events.send(CreateDeuxZeroEvent.OnFinished) }
        }
    }

    private fun manageCampAEffectif() {
        val camp = _state.value.createdCampA ?: return
        viewModelScope.launch { _events.send(CreateDeuxZeroEvent.OnManageEffectif(camp, clubId)) }
    }

    private fun submit() {
        val input = _state.value.toInput()
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val deuxZero = createDeuxZeroUseCase(input).onErrorUpdateState() ?: return@launch
            val camps = listCampsUseCase(deuxZero.id).onErrorUpdateState() ?: return@launch
            val campA = camps.getOrNull(0)
            val campB = camps.getOrNull(1)
            if (campA == null || campB == null) {
                _state.update { it.copy(isLoading = false, errorMessage = UiText.DynamicString("Le 2-0 a été créé mais ses camps sont introuvables.")) }
                return@launch
            }

            val engagedCampA = engageClubUseCase(campA.codeEngagement).onErrorUpdateState() ?: return@launch

            if (_state.value.estInterne) {
                val engagedCampB = engageClubUseCase(campB.codeEngagement).onErrorUpdateState() ?: return@launch
                _state.update { it.copy(isLoading = false) }
                _events.send(CreateDeuxZeroEvent.OnInterneCreated(deuxZero.id, clubId, engagedCampA, engagedCampB))
            } else {
                _state.update { it.copy(isLoading = false, codeCampBToShare = campB.codeEngagement, createdCampA = engagedCampA) }
            }
        }
    }

    private fun <T> Result<T?, DeuxZeroError>.onErrorUpdateState(): T? = when (this) {
        is Result.Success -> data ?: run {
            _state.update { it.copy(isLoading = false, errorMessage = UiText.DynamicString("Une erreur est survenue. Réessaie.")) }
            null
        }
        is Result.Failure -> {
            _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) }
            null
        }
    }

    private fun CreateDeuxZeroState.toInput() = CreateDeuxZeroInput(
        nom = nom.trim(),
        lieu = lieu.trim().ifBlank { null },
        visibilite = visibilite,
        estInterne = estInterne,
        joursHabituels = joursHabituels.toList(),
        heureHabituelle = heureHabituelle.trim().ifBlank { null },
        dureeMiTempsMinutes = dureeMiTempsMinutes,
        nombreMiTemps = nombreMiTemps,
        dureePauseMinutes = dureePauseMinutes,
        nombreMaxJoueursParCamp = nombreMaxJoueursParCamp,
        delaiValidationAutomatiqueHeures = delaiValidationAutomatiqueHeures,
        transfertsAutorisesParSaison = transfertsAutorisesParSaison,
        libelleCampA = libelleCampA.trim(),
        libelleCampB = libelleCampB.trim(),
        libelleSaison = libelleSaison.trim(),
        dateDebutSaison = dateDebutSaison.trim(),
    )
}
