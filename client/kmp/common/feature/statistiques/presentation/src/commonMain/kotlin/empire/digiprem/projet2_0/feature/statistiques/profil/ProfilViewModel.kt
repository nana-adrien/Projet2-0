package empire.digiprem.projet2_0.feature.statistiques.profil

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.statistiques.extension.toUiText
import empire.digiprem.projet2_0.feature.statistiques.usecase.GetProfilJoueurUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E19 — Profil et progression (CU23). */
class ProfilViewModel(
    private val getProfilJoueurUseCase: GetProfilJoueurUseCase,
    private val authSessionStorage: AuthSessionStorage,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(ProfilState())
    val state = _state.asStateFlow()

    private var hasInitialized = false

    fun onAction(action: ProfilAction) {
        when (action) {
            is ProfilAction.OnInit -> init(action.utilisateurId, action.deuxZeroId)
            ProfilAction.OnRetry -> charger()
            is ProfilAction.OnContexteChange -> {
                _state.update { it.copy(contexte = action.contexte) }
                charger()
            }
            ProfilAction.OnSignalerClick -> _state.update { it.copy(showSignaler = true) }
            ProfilAction.OnDismissSignaler -> _state.update { it.copy(showSignaler = false) }
        }
    }

    private fun init(utilisateurId: String?, deuxZeroId: String) {
        _state.update { it.copy(deuxZeroId = deuxZeroId, estMonPropreProfil = utilisateurId == null) }
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch {
            val resolvedId = utilisateurId ?: authSessionStorage.getData()?.userId.orEmpty()
            _state.update { it.copy(utilisateurId = resolvedId) }
            charger()
        }
    }

    private fun charger() {
        val utilisateurId = _state.value.utilisateurId
        if (utilisateurId.isBlank()) return
        val deuxZeroId = _state.value.deuxZeroId
        val contexte = _state.value.contexte
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getProfilJoueurUseCase(utilisateurId, deuxZeroId, contexte)
                .onSuccess { profil -> _state.update { it.copy(isLoading = false, profil = profil) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }
}
