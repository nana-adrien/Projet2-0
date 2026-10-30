package empire.digiprem.projet2_0.feature.matchendirect.marqueur

import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.nowEpochMillis
import empire.digiprem.projet2_0.core.domain.util.onFailure
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.core.presentation.util.AbstractViewModel
import empire.digiprem.projet2_0.feature.deuxzero.extension.toUiText as toDeuxZeroUiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListEffectifUseCase
import empire.digiprem.projet2_0.feature.matchendirect.extension.toUiText
import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenementSaisissable
import empire.digiprem.projet2_0.feature.matchendirect.usecase.AjouterEvenementUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.AnnulerDernierEvenementUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.DemarrerMatchUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.GetMatchEnDirectUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.ListCompositionUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.PasserMiTempsUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.TerminerMatchUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** E10/E11 — Écran marqueur, match en cours (CU17). */
class MarqueurViewModel(
    private val listCampsUseCase: ListCampsUseCase,
    private val listEffectifUseCase: ListEffectifUseCase,
    private val listCompositionUseCase: ListCompositionUseCase,
    private val getMatchEnDirectUseCase: GetMatchEnDirectUseCase,
    private val demarrerMatchUseCase: DemarrerMatchUseCase,
    private val ajouterEvenementUseCase: AjouterEvenementUseCase,
    private val annulerDernierEvenementUseCase: AnnulerDernierEvenementUseCase,
    private val passerMiTempsUseCase: PasserMiTempsUseCase,
    private val terminerMatchUseCase: TerminerMatchUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(MarqueurState())
    val state = _state.asStateFlow()

    private val _events = Channel<MarqueurEvent>()
    val events = _events.receiveAsFlow()

    private var hasInitialized = false

    fun onAction(action: MarqueurAction) {
        when (action) {
            is MarqueurAction.OnInit -> init(action.matchId, action.deuxZeroId)
            MarqueurAction.OnRetry -> load()
            MarqueurAction.OnDemarrerMatchClick -> demarrerMatch()
            is MarqueurAction.OnGoalButtonClick -> _state.update { it.copy(sheet = MarqueurSheet.But(action.camp)) }
            MarqueurAction.OnAutreClick -> _state.update { it.copy(sheet = MarqueurSheet.Autre) }
            is MarqueurAction.OnAutreTypeClick ->
                _state.update { it.copy(sheet = MarqueurSheet.JoueurUnique(action.camp, action.type, action.titre)) }
            is MarqueurAction.OnRemplacementClick -> _state.update { it.copy(sheet = MarqueurSheet.Remplacement(action.camp)) }
            MarqueurAction.OnPasserMiTempsClick -> passerMiTemps()
            MarqueurAction.OnAnnulerDernierClick -> annulerDernier()
            MarqueurAction.OnFinDuMatchClick -> _state.update { it.copy(showConfirmerFin = true) }
            MarqueurAction.OnConfirmerFinDuMatch -> terminerMatch()
            MarqueurAction.OnDismissConfirmerFin -> _state.update { it.copy(showConfirmerFin = false) }
            MarqueurAction.OnDismissSheet -> _state.update { it.copy(sheet = null) }
            is MarqueurAction.OnEnregistrerBut -> enregistrerBut(action.buteurId, action.passeurId, action.contreSonCamp)
            is MarqueurAction.OnEnregistrerJoueurUnique -> enregistrerJoueurUnique(action.joueurId)
            is MarqueurAction.OnEnregistrerRemplacement -> enregistrerRemplacement(action.sortantId, action.entrantId)
            MarqueurAction.OnMessageShown -> _state.update { it.copy(message = null, messageActionLabel = null) }
        }
    }

    private fun init(matchId: String, deuxZeroId: String) {
        _state.update { it.copy(matchId = matchId, deuxZeroId = deuxZeroId) }
        if (hasInitialized) return
        hasInitialized = true
        load()
        demarrerChrono()
    }

    private fun demarrerChrono() {
        viewModelScope.launch {
            while (true) {
                _state.update { it.copy(nowEpochMillis = nowEpochMillis()) }
                delay(1_000)
            }
        }
    }

    private fun load() {
        val deuxZeroId = _state.value.deuxZeroId
        val matchId = _state.value.matchId
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            listCampsUseCase(deuxZeroId)
                .onSuccess { camps ->
                    val campA = camps?.getOrNull(0)
                    val campB = camps?.getOrNull(1)
                    _state.update { it.copy(campA = campA, campB = campB) }
                    campA?.let { chargerEffectif(it, estCampA = true) }
                    campB?.let { chargerEffectif(it, estCampA = false) }
                }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toDeuxZeroUiText()) } }

            listCompositionUseCase(matchId)
                .onSuccess { compositions -> _state.update { it.copy(compositions = compositions.orEmpty()) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.toUiText()) } }

            getMatchEnDirectUseCase(matchId)
                .onSuccess { matchEnDirect -> appliquer(matchEnDirect) }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUiText()) } }
        }
    }

    private suspend fun chargerEffectif(camp: Camp, estCampA: Boolean) {
        listEffectifUseCase(camp.id)
            .onSuccess { effectif ->
                _state.update { if (estCampA) it.copy(effectifA = effectif.orEmpty()) else it.copy(effectifB = effectif.orEmpty()) }
            }
            .onFailure { error -> _state.update { it.copy(errorMessage = error.toDeuxZeroUiText()) } }
    }

    private fun appliquer(matchEnDirect: MatchEnDirect?, message: UiText? = null, actionLabel: String? = null) {
        _state.update {
            it.copy(
                isLoading = false,
                isActionLoading = false,
                match = matchEnDirect?.match ?: it.match,
                evenements = matchEnDirect?.evenements ?: it.evenements,
                sheet = null,
                message = message ?: it.message,
                messageActionLabel = actionLabel,
            )
        }
    }

    private fun demarrerMatch() {
        val matchId = _state.value.matchId
        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            demarrerMatchUseCase(matchId)
                .onSuccess { appliquer(it, UiText.DynamicString("Coup d'envoi donné.")) }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }

    private fun enregistrerBut(buteurId: String?, passeurId: String?, contreSonCamp: Boolean) {
        val sheet = _state.value.sheet as? MarqueurSheet.But ?: return
        if (buteurId == null) return
        val matchId = _state.value.matchId
        val minute = _state.value.minuteActuelle
        val periode = _state.value.periodeActuelle
        val type = if (contreSonCamp) TypeEvenementSaisissable.BUT_CONTRE_SON_CAMP else TypeEvenementSaisissable.BUT
        val campId = if (contreSonCamp) _state.value.campAdverse(sheet.camp)?.id ?: sheet.camp.id else sheet.camp.id

        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            ajouterEvenementUseCase(matchId, type, minute, periode, campId, buteurId, passeurId)
                .onSuccess { appliquer(it, UiText.DynamicString("But enregistré."), actionLabel = "Annuler") }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }

    private fun enregistrerJoueurUnique(joueurId: String) {
        val sheet = _state.value.sheet as? MarqueurSheet.JoueurUnique ?: return
        val matchId = _state.value.matchId
        val minute = _state.value.minuteActuelle
        val periode = _state.value.periodeActuelle

        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            ajouterEvenementUseCase(matchId, sheet.type, minute, periode, sheet.camp.id, joueurId, null)
                .onSuccess { appliquer(it, UiText.DynamicString("${sheet.titre} enregistré."), actionLabel = "Annuler") }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }

    private fun enregistrerRemplacement(sortantId: String, entrantId: String) {
        val sheet = _state.value.sheet as? MarqueurSheet.Remplacement ?: return
        val matchId = _state.value.matchId
        val minute = _state.value.minuteActuelle
        val periode = _state.value.periodeActuelle

        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            ajouterEvenementUseCase(matchId, TypeEvenementSaisissable.REMPLACEMENT, minute, periode, sheet.camp.id, sortantId, entrantId)
                .onSuccess { appliquer(it, UiText.DynamicString("Remplacement enregistré."), actionLabel = "Annuler") }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }

    private fun passerMiTemps() {
        val matchId = _state.value.matchId
        val periode = _state.value.periodeActuelle
        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            passerMiTempsUseCase(matchId, periode)
                .onSuccess { appliquer(it, UiText.DynamicString("Mi-temps suivante lancée.")) }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }

    private fun annulerDernier() {
        val matchId = _state.value.matchId
        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            annulerDernierEvenementUseCase(matchId)
                .onSuccess { appliquer(it, UiText.DynamicString("Dernier événement annulé.")) }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }

    private fun terminerMatch() {
        val matchId = _state.value.matchId
        val periode = _state.value.periodeActuelle
        _state.update { it.copy(isActionLoading = true) }
        viewModelScope.launch {
            terminerMatchUseCase(matchId, periode)
                .onSuccess {
                    appliquer(it)
                    _events.send(MarqueurEvent.OnMatchTermine)
                }
                .onFailure { error -> _state.update { it.copy(isActionLoading = false, message = error.toUiText()) } }
        }
    }
}
