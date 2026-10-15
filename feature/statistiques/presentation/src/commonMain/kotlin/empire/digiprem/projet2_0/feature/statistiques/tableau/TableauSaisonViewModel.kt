package empire.digiprem.projet2_0.feature.statistiques.tableau

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.SaisonStatut
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListSaisonsUseCase
import empire.digiprem.projet2_0.feature.statistiques.extension.toUiText
import empire.digiprem.projet2_0.feature.statistiques.usecase.GetTableauSaisonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E17 — Tableau de la saison (CU22). */
class TableauSaisonViewModel(
    private val listCampsUseCase: ListCampsUseCase,
    private val listSaisonsUseCase: ListSaisonsUseCase,
    private val getTableauSaisonUseCase: GetTableauSaisonUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(TableauSaisonState())
    val state = _state.asStateFlow()

    private var hasInitialized = false

    fun onAction(action: TableauSaisonAction) {
        when (action) {
            is TableauSaisonAction.OnInit -> init(action.deuxZeroId)
            TableauSaisonAction.OnRetry -> chargerTableau()
            is TableauSaisonAction.OnSaisonChange -> {
                _state.update { it.copy(saisonSelectionneeId = action.saisonId) }
                chargerTableau()
            }
        }
    }

    private fun init(deuxZeroId: String) {
        _state.update { it.copy(deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch {
            listCampsUseCase(deuxZeroId)
                .onSuccess { camps -> _state.update { it.copy(campA = camps?.getOrNull(0), campB = camps?.getOrNull(1)) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toDeuxZeroUiText()) } }

            listSaisonsUseCase(deuxZeroId)
                .onSuccess { saisons ->
                    val courante = saisons.orEmpty().firstOrNull { it.statut == SaisonStatut.EN_COURS } ?: saisons.orEmpty().firstOrNull()
                    _state.update { it.copy(saisons = saisons.orEmpty(), saisonSelectionneeId = courante?.id.orEmpty()) }
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
}
