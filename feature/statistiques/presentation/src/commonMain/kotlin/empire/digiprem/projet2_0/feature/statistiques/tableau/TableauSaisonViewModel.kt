package empire.digiprem.projet2_0.feature.statistiques.tableau

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.SaisonStatut
import empire.digiprem.projet2_0.feature.deuxzero.usecase.CloseSaisonUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListMesDeuxZeroUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListSaisonsUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.OpenSaisonUseCase
import empire.digiprem.projet2_0.feature.statistiques.extension.toUiText
import empire.digiprem.projet2_0.feature.statistiques.usecase.GetTableauSaisonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E17 — Tableau de la saison (CU22). CU12 — ouvrir/clôturer une saison. */
class TableauSaisonViewModel(
    private val listCampsUseCase: ListCampsUseCase,
    private val listSaisonsUseCase: ListSaisonsUseCase,
    private val getTableauSaisonUseCase: GetTableauSaisonUseCase,
    private val listMesDeuxZeroUseCase: ListMesDeuxZeroUseCase,
    private val openSaisonUseCase: OpenSaisonUseCase,
    private val closeSaisonUseCase: CloseSaisonUseCase,
    private val authSessionStorage: AuthSessionStorage,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(TableauSaisonState())
    val state = _state.asStateFlow()

    private var hasInitialized = false
    private var currentUserId: String? = null

    fun onAction(action: TableauSaisonAction) {
        when (action) {
            is TableauSaisonAction.OnInit -> init(action.deuxZeroId)
            TableauSaisonAction.OnRetry -> chargerTableau()
            is TableauSaisonAction.OnSaisonChange -> {
                _state.update { it.copy(saisonSelectionneeId = action.saisonId) }
                chargerTableau()
            }
            TableauSaisonAction.OnOuvrirSaisonClick -> _state.update { it.copy(showOuvrirDialog = true) }
            TableauSaisonAction.OnDismissOuvrirDialog -> _state.update { it.copy(showOuvrirDialog = false) }
            is TableauSaisonAction.OnConfirmOuvrirSaison -> ouvrirSaison(action.libelle, action.dateDebut, action.dateFin)
            TableauSaisonAction.OnCloturerSaisonClick -> _state.update { it.copy(showCloturerDialog = true) }
            TableauSaisonAction.OnDismissCloturerDialog -> _state.update { it.copy(showCloturerDialog = false) }
            TableauSaisonAction.OnConfirmCloturerSaison -> cloturerSaison()
            TableauSaisonAction.OnMessageShown -> _state.update { it.copy(message = null) }
        }
    }

    private fun init(deuxZeroId: String) {
        _state.update { it.copy(deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch {
            currentUserId = authSessionStorage.getData()?.userId
            listCampsUseCase(deuxZeroId)
                .onSuccess { camps -> _state.update { it.copy(campA = camps?.getOrNull(0), campB = camps?.getOrNull(1)) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toDeuxZeroUiText()) } }

            listMesDeuxZeroUseCase()
                .onSuccess { deuxZeros ->
                    val estOrganisateur = deuxZeros.orEmpty().any { it.id == deuxZeroId && it.organisateurId == currentUserId }
                    _state.update { it.copy(peutGererSaisons = estOrganisateur) }
                }
                .onFailure { /* non bloquant : les boutons de gestion de saison restent masqués */ }

            chargerSaisons()
        }
    }

    private fun chargerSaisons() {
        val deuxZeroId = _state.value.deuxZeroId
        viewModelScope.launch {
            listSaisonsUseCase(deuxZeroId)
                .onSuccess { saisons ->
                    val selectionActuelle = _state.value.saisonSelectionneeId
                    val conserver = saisons.orEmpty().any { it.id == selectionActuelle }
                    val courante = saisons.orEmpty().firstOrNull { it.statut == SaisonStatut.EN_COURS } ?: saisons.orEmpty().firstOrNull()
                    _state.update {
                        it.copy(saisons = saisons.orEmpty(), saisonSelectionneeId = if (conserver) selectionActuelle else courante?.id.orEmpty())
                    }
                    chargerTableau()
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toDeuxZeroUiText()) } }
        }
    }

    private fun chargerTableau() {
        val saisonId = _state.value.saisonSelectionneeId
        if (saisonId.isBlank()) return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getTableauSaisonUseCase(saisonId)
                .onSuccess { tableau -> _state.update { it.copy(isLoading = false, tableau = tableau) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun ouvrirSaison(libelle: String, dateDebut: String, dateFin: String?) {
        val deuxZeroId = _state.value.deuxZeroId
        _state.update { it.copy(isSaving = true, showOuvrirDialog = false) }
        viewModelScope.launch {
            openSaisonUseCase(deuxZeroId, libelle, dateDebut, dateFin)
                .onSuccess { saison ->
                    _state.update { it.copy(isSaving = false, saisonSelectionneeId = saison?.id.orEmpty(), message = UiText.DynamicString("Nouvelle saison ouverte.")) }
                    chargerSaisons()
                }
                .onFailure { error -> _state.update { it.copy(isSaving = false, message = error.toDeuxZeroUiText()) } }
        }
    }

    private fun cloturerSaison() {
        val saisonId = _state.value.saisonSelectionneeId
        _state.update { it.copy(isSaving = true, showCloturerDialog = false) }
        viewModelScope.launch {
            closeSaisonUseCase(saisonId)
                .onSuccess {
                    _state.update { it.copy(isSaving = false, message = UiText.DynamicString("Saison clôturée. Le champion et le bilan sont calculés.")) }
                    chargerSaisons()
                }
                .onFailure { error -> _state.update { it.copy(isSaving = false, message = error.toDeuxZeroUiText()) } }
        }
    }
}
