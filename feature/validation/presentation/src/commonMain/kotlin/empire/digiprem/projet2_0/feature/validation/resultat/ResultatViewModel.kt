package empire.digiprem.projet2_0.feature.validation.resultat

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import empire.digiprem.projet2_0.feature.validation.extension.toUiText
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch
import empire.digiprem.projet2_0.feature.validation.usecase.GetResultatUseCase
import empire.digiprem.projet2_0.feature.validation.usecase.RepondreContestationUseCase
import empire.digiprem.projet2_0.feature.validation.usecase.ValiderResultatUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E14 — Résultat à valider (CU19). */
class ResultatViewModel(
    private val listCampsUseCase: ListCampsUseCase,
    private val getResultatUseCase: GetResultatUseCase,
    private val validerResultatUseCase: ValiderResultatUseCase,
    private val repondreContestationUseCase: RepondreContestationUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(ResultatState())
    val state = _state.asStateFlow()

    private val _events = Channel<ResultatEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: ResultatAction) {
        when (action) {
            is ResultatAction.OnInit -> init(action.camp, action.matchId, action.deuxZeroId)
            ResultatAction.OnRetry -> load()
            ResultatAction.OnValiderClick -> _state.update { it.copy(showConfirmerValidation = true) }
            ResultatAction.OnConfirmerValidation -> valider()
            ResultatAction.OnDismissConfirmerValidation -> _state.update { it.copy(showConfirmerValidation = false) }
            ResultatAction.OnContesterClick -> viewModelScope.launch { _events.send(ResultatEvent.OnNavigateToContester) }
            ResultatAction.OnAccepterCorrectionClick -> _state.update { it.copy(showConfirmerAcceptation = true) }
            ResultatAction.OnConfirmerAcceptation -> repondre(accepter = true, commentaire = null)
            ResultatAction.OnDismissConfirmerAcceptation -> _state.update { it.copy(showConfirmerAcceptation = false) }
            ResultatAction.OnRefuserCorrectionClick -> _state.update { it.copy(showRefuser = true) }
            is ResultatAction.OnConfirmerRefus -> repondre(accepter = false, commentaire = action.commentaire)
            ResultatAction.OnDismissRefuser -> _state.update { it.copy(showRefuser = false) }
            ResultatAction.OnMessageShown -> _state.update { it.copy(message = null) }
        }
    }

    private fun init(camp: Camp, matchId: String, deuxZeroId: String) {
        _state.update { it.copy(monCamp = camp, matchId = matchId, deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        load()
    }

    fun load() {
        val deuxZeroId = _state.value.deuxZeroId
        val matchId = _state.value.matchId
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            listCampsUseCase(deuxZeroId)
                .onSuccess { camps -> _state.update { it.copy(campA = camps?.getOrNull(0), campB = camps?.getOrNull(1)) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toDeuxZeroUiText()) } }

            getResultatUseCase(matchId)
                .onSuccess { resultat -> _state.update { it.copy(isLoading = false, resultat = resultat) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }

    private fun appliquer(resultat: ResultatMatch?, message: UiText? = null) {
        _state.update {
            it.copy(
                isActionLoading = false,
                resultat = resultat ?: it.resultat,
                showConfirmerValidation = false,
                showConfirmerAcceptation = false,
                showRefuser = false,
                message = message ?: it.message,
            )
        }
    }

    private fun valider() {
        val matchId = _state.value.matchId
        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            validerResultatUseCase(matchId)
                .onSuccess { appliquer(it, UiText.DynamicString("Résultat validé.")) }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }

    private fun repondre(accepter: Boolean, commentaire: String?) {
        val matchId = _state.value.matchId
        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            repondreContestationUseCase(matchId, accepter, commentaire)
                .onSuccess {
                    val message = if (accepter) "Correction acceptée, match officialisé." else "Correction refusée, litige transmis à l'organisateur."
                    appliquer(it, UiText.DynamicString(message))
                }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }
}
