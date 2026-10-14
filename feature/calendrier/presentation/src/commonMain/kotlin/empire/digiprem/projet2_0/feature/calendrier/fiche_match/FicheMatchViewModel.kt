package empire.digiprem.projet2_0.feature.calendrier.fiche_match

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.calendrier.extension.toUiText
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.calendrier.model.StatutMatchModifiable
import empire.digiprem.projet2_0.feature.calendrier.usecase.GenererConvocationsUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.ListConvocationsUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.ModifierMatchUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.RelancerConvocationsUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.RepondreConvocationUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListMesDeuxZeroUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E08 — Fiche du match et présences (CU15). */
class FicheMatchViewModel(
    private val listConvocationsUseCase: ListConvocationsUseCase,
    private val genererConvocationsUseCase: GenererConvocationsUseCase,
    private val relancerConvocationsUseCase: RelancerConvocationsUseCase,
    private val repondreConvocationUseCase: RepondreConvocationUseCase,
    private val modifierMatchUseCase: ModifierMatchUseCase,
    private val listCampsUseCase: ListCampsUseCase,
    private val listMesDeuxZeroUseCase: ListMesDeuxZeroUseCase,
    private val authSessionStorage: AuthSessionStorage,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(FicheMatchState())
    val state = _state.asStateFlow()

    private var hasInitialized = false

    fun onAction(action: FicheMatchAction) {
        when (action) {
            is FicheMatchAction.OnInit -> init(action.match, action.deuxZeroId)
            FicheMatchAction.OnRetry -> load()
            FicheMatchAction.OnGenererConvocationsClick -> genererConvocations()
            FicheMatchAction.OnRelancerClick -> relancer()
            is FicheMatchAction.OnRepondre -> repondre(action.reponse)
            FicheMatchAction.OnShowModifierDialog -> _state.update { it.copy(showModifierDialog = true) }
            FicheMatchAction.OnDismissModifierDialog -> _state.update { it.copy(showModifierDialog = false) }
            is FicheMatchAction.OnModifierConfirm ->
                modifierMatch(action.nouveauStatut, action.nouvelleDate, action.nouvelleHeure, action.motif)
            FicheMatchAction.OnMessageShown -> _state.update { it.copy(message = null) }
        }
    }

    private fun init(match: Match, deuxZeroId: String) {
        _state.update { it.copy(match = match, deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        viewModelScope.launch { _state.update { it.copy(currentUserId = authSessionStorage.getData()?.userId) } }
        load()
    }

    private fun load() {
        val matchId = _state.value.match?.id ?: return
        val deuxZeroId = _state.value.deuxZeroId
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            listConvocationsUseCase(matchId)
                .onSuccess { convocations -> _state.update { it.copy(isLoading = false, convocations = convocations.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }

            listCampsUseCase(deuxZeroId)
                .onSuccess { camps -> _state.update { it.copy(camps = camps.orEmpty()) } }
                .onFailure { /* non bloquant : les boutons composition/marqueur restent simplement masqués */ }

            val currentUserId = _state.value.currentUserId
            listMesDeuxZeroUseCase()
                .onSuccess { deuxZeros ->
                    val estOrganisateur = deuxZeros.orEmpty().any { it.id == deuxZeroId && it.organisateurId == currentUserId }
                    _state.update { it.copy(suisOrganisateur = estOrganisateur) }
                }
                .onFailure { /* non bloquant : le bouton d'arbitrage reste simplement masqué */ }
        }
    }

    private fun genererConvocations() {
        val matchId = _state.value.match?.id ?: return
        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            genererConvocationsUseCase(matchId)
                .onSuccess { _state.update { it.copy(isActionLoading = false, message = UiText.DynamicString("Convocations envoyées.")) }; load() }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }

    private fun relancer() {
        val matchId = _state.value.match?.id ?: return
        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            relancerConvocationsUseCase(matchId)
                .onSuccess { _state.update { it.copy(isActionLoading = false, message = UiText.DynamicString("Relance envoyée.")) } }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }

    private fun repondre(reponse: ReponseConvocation) {
        val convocationId = _state.value.maConvocation?.id ?: return
        viewModelScope.launch {
            repondreConvocationUseCase(convocationId, reponse)
                .onSuccess { load() }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }

    private fun modifierMatch(nouveauStatut: StatutMatchModifiable, nouvelleDate: String?, nouvelleHeure: String?, motif: String?) {
        val matchId = _state.value.match?.id ?: return
        viewModelScope.launch {
            modifierMatchUseCase(matchId, nouveauStatut, nouvelleDate, nouvelleHeure, motif)
                .onSuccess { match ->
                    _state.update { it.copy(match = match ?: it.match, showModifierDialog = false, message = UiText.DynamicString("Match mis à jour.")) }
                }
                .onFailure { error -> _state.update { it.copy(message = error.toUiText()) } }
        }
    }
}
