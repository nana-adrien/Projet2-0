package empire.digiprem.projet2_0.feature.statistiques.bilan

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import empire.digiprem.projet2_0.feature.statistiques.extension.toUiText
import empire.digiprem.projet2_0.feature.statistiques.usecase.GetBilanSaisonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E21 — Bilan de la saison (CU25). */
class BilanViewModel(
    private val listCampsUseCase: ListCampsUseCase,
    private val getBilanSaisonUseCase: GetBilanSaisonUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(BilanState())
    val state = _state.asStateFlow()

    private var hasInitialized = false

    fun onAction(action: BilanAction) {
        when (action) {
            is BilanAction.OnInit -> init(action.saisonId, action.deuxZeroId)
            BilanAction.OnRetry -> charger()
        }
    }

    private fun init(saisonId: String, deuxZeroId: String) {
        _state.update { it.copy(saisonId = saisonId, deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch {
            listCampsUseCase(deuxZeroId)
                .onSuccess { camps -> _state.update { it.copy(campA = camps?.getOrNull(0), campB = camps?.getOrNull(1)) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toDeuxZeroUiText()) } }

            charger()
        }
    }

    private fun charger() {
        val saisonId = _state.value.saisonId
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getBilanSaisonUseCase(saisonId)
                .onSuccess { bilan -> _state.update { it.copy(isLoading = false, bilan = bilan) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }
}
