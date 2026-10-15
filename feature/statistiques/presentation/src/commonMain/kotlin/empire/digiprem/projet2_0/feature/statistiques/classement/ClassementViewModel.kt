package empire.digiprem.projet2_0.feature.statistiques.classement

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.SaisonStatut
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListSaisonsUseCase
import empire.digiprem.projet2_0.feature.statistiques.extension.toUiText
import empire.digiprem.projet2_0.feature.statistiques.usecase.GetClassementUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E18 — Classements des joueurs (CU22). */
class ClassementViewModel(
    private val listSaisonsUseCase: ListSaisonsUseCase,
    private val getClassementUseCase: GetClassementUseCase,
    private val authSessionStorage: AuthSessionStorage,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(ClassementState())
    val state = _state.asStateFlow()

    private var hasInitialized = false

    fun onAction(action: ClassementAction) {
        when (action) {
            is ClassementAction.OnInit -> init(action.deuxZeroId)
            ClassementAction.OnRetry -> charger()
            is ClassementAction.OnTypeChange -> {
                _state.update { it.copy(type = action.type) }
                charger()
            }
        }
    }

    private fun init(deuxZeroId: String) {
        _state.update { it.copy(deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch {
            _state.update { it.copy(currentUserId = authSessionStorage.getData()?.userId) }

            listSaisonsUseCase(deuxZeroId)
                .onSuccess { saisons ->
                    val courante = saisons.orEmpty().firstOrNull { it.statut == SaisonStatut.EN_COURS } ?: saisons.orEmpty().firstOrNull()
                    _state.update { it.copy(saisonId = courante?.id.orEmpty()) }
                    charger()
                }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toDeuxZeroUiText()) } }
        }
    }

    private fun charger() {
        val saisonId = _state.value.saisonId
        if (saisonId.isBlank()) return
        val type = _state.value.type
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getClassementUseCase(saisonId, type)
                .onSuccess { lignes -> _state.update { it.copy(isLoading = false, lignes = lignes.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }
}
