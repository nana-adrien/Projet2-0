package empire.digiprem.projet2_0.feature.validation.contester

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.validation.extension.toUiText
import empire.digiprem.projet2_0.feature.validation.usecase.ContesterResultatUseCase
import empire.digiprem.projet2_0.feature.validation.usecase.GetResultatUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E15 — Contester et proposer une correction (CU19, A1). */
class ContesterViewModel(
    private val getResultatUseCase: GetResultatUseCase,
    private val contesterResultatUseCase: ContesterResultatUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(ContesterState())
    val state = _state.asStateFlow()

    private val _events = Channel<ContesterEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: ContesterAction) {
        when (action) {
            is ContesterAction.OnInit -> init(action.matchId)
            is ContesterAction.OnTypeErreurChange -> _state.update { it.copy(typeErreur = action.typeErreur, evenementACorrigerId = null, bonJoueur = "") }
            is ContesterAction.OnEvenementChange -> _state.update { it.copy(evenementACorrigerId = action.evenementId) }
            is ContesterAction.OnBonJoueurChange -> _state.update { it.copy(bonJoueur = action.value) }
            is ContesterAction.OnCommentaireChange -> _state.update { it.copy(commentaire = action.value) }
            ContesterAction.OnEnvoyerClick -> envoyer()
        }
    }

    private fun init(matchId: String) {
        _state.update { it.copy(matchId = matchId) }
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch {
            getResultatUseCase(matchId)
                .onSuccess { resultat -> _state.update { it.copy(evenements = resultat?.evenements.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toUiText()) } }
        }
    }

    private fun envoyer() {
        val current = _state.value
        if (!current.peutEnvoyer) return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            contesterResultatUseCase(current.matchId, current.construireCorrectionProposee(), current.commentaire.ifBlank { null })
                .onSuccess { viewModelScope.launch { _events.send(ContesterEvent.OnEnvoye) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }
}
