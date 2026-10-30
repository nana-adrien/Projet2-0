package empire.digiprem.projet2_0.feature.validation.resultat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.feedback.PDZToast
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.timeline.Timeline
import empire.digiprem.projet2_0.core.design_system.components.timeline.TimelineEntry
import empire.digiprem.projet2_0.core.design_system.components.timeline.TimelineSide
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenement
import empire.digiprem.projet2_0.feature.validation.model.DecisionValidation
import empire.digiprem.projet2_0.feature.validation.model.Validation
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResultatRoot(
    camp: Camp,
    matchId: String,
    deuxZeroId: String,
    onContester: () -> Unit,
    viewModel: ResultatViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(camp.id, matchId) { viewModel.onAction(ResultatAction.OnInit(camp, matchId, deuxZeroId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            ResultatEvent.OnNavigateToContester -> onContester()
        }
    }

    ResultatScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun ResultatScreen(
    state: ResultatState,
    onAction: (ResultatAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold { padding ->
            when {
                state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())

                else -> LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = PaddingValues(PDZSpacing.space4),
                ) {
                    item {
                        Text(text = "Valider le résultat", style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(PDZSpacing.space4))
                        ScoreBoard(state = state)
                        Spacer(Modifier.height(PDZSpacing.space4))
                    }

                    item {
                        Timeline(
                            entries = state.resultat?.evenements.orEmpty()
                                .filterNot { it.annule }
                                .filter { it.type != TypeEvenement.DEBUT_PERIODE && it.type != TypeEvenement.FIN_PERIODE }
                                .sortedByDescending { it.minute }
                                .map { it.toTimelineEntry(state.campA?.id) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(PDZSpacing.space4))
                    }

                    item {
                        Text(text = "Signatures", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(PDZSpacing.space2))
                        ValidationRow(camp = state.campA, validation = state.validationAffichee(state.campA?.id), estMonCamp = state.campA?.id == state.monCamp?.id)
                        ValidationRow(camp = state.campB, validation = state.validationAffichee(state.campB?.id), estMonCamp = state.campB?.id == state.monCamp?.id)
                        Spacer(Modifier.height(PDZSpacing.space6))
                    }

                    item {
                        when {
                            state.enModeReponseContestation -> ReponseContestationActions(state = state, onAction = onAction)
                            state.maValidation == null -> Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space3)) {
                                PDZButton(
                                    text = "Contester",
                                    type = PDZButtonType.Secondary,
                                    onClick = { onAction(ResultatAction.OnContesterClick) },
                                    modifier = Modifier.weight(1f),
                                )
                                PDZButton(
                                    text = "Valider",
                                    isLoading = state.isActionLoading,
                                    onClick = { onAction(ResultatAction.OnValiderClick) },
                                    modifier = Modifier.weight(2f),
                                )
                            }
                            else -> Text(
                                text = "Ta signature est enregistrée.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

        state.message?.let { message ->
            LaunchedEffect(message) {
                delay(3_000)
                onAction(ResultatAction.OnMessageShown)
            }
            PDZToast(message = message.asString(), modifier = Modifier.align(Alignment.BottomCenter).padding(PDZSpacing.space4))
        }
    }

    if (state.showConfirmerValidation) {
        AlertDialog(
            onDismissRequest = { onAction(ResultatAction.OnDismissConfirmerValidation) },
            title = { Text("Valider ce résultat ?") },
            text = { Text("C'est ta signature officielle pour ton camp. Cette action ne peut pas être annulée.") },
            confirmButton = { TextButton(onClick = { onAction(ResultatAction.OnConfirmerValidation) }) { Text("Valider") } },
            dismissButton = { TextButton(onClick = { onAction(ResultatAction.OnDismissConfirmerValidation) }) { Text("Annuler") } },
        )
    }

    if (state.showConfirmerAcceptation) {
        AlertDialog(
            onDismissRequest = { onAction(ResultatAction.OnDismissConfirmerAcceptation) },
            title = { Text("Accepter la correction ?") },
            text = { Text("Le match sera corrigé puis officialisé.") },
            confirmButton = { TextButton(onClick = { onAction(ResultatAction.OnConfirmerAcceptation) }) { Text("Accepter") } },
            dismissButton = { TextButton(onClick = { onAction(ResultatAction.OnDismissConfirmerAcceptation) }) { Text("Annuler") } },
        )
    }

    if (state.showRefuser) {
        var commentaire by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { onAction(ResultatAction.OnDismissRefuser) },
            title = { Text("Refuser la correction") },
            text = {
                Column {
                    Text("Le litige sera transmis à l'organisateur. Explique brièvement pourquoi.")
                    Spacer(Modifier.height(PDZSpacing.space3))
                    OutlinedTextField(value = commentaire, onValueChange = { commentaire = it }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(enabled = commentaire.isNotBlank(), onClick = { onAction(ResultatAction.OnConfirmerRefus(commentaire)) }) { Text("Refuser") }
            },
            dismissButton = { TextButton(onClick = { onAction(ResultatAction.OnDismissRefuser) }) { Text("Annuler") } },
        )
    }
}

@Composable
private fun ScoreBoard(state: ResultatState) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = state.campA?.libelle.orEmpty(), style = MaterialTheme.typography.titleMedium)
        Text(
            text = "${state.resultat?.match?.scoreCampA ?: 0} – ${state.resultat?.match?.scoreCampB ?: 0}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(text = state.campB?.libelle.orEmpty(), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun ValidationRow(camp: Camp?, validation: Validation?, estMonCamp: Boolean) {
    val libelle = camp?.libelle.orEmpty() + if (estMonCamp) " (moi)" else ""
    val statut = when {
        validation == null -> "En attente de signature"
        validation.decision == DecisionValidation.VALIDE -> "Validé par ${validation.signatairePseudo}"
        else -> "Contesté par ${validation.signatairePseudo}"
    }
    Text(text = "$libelle — $statut", style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun ReponseContestationActions(state: ResultatState, onAction: (ResultatAction) -> Unit) {
    Column {
        Text(text = "Correction proposée par l'autre camp :", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(PDZSpacing.space2))
        Text(text = state.contestationAdverse?.correctionProposee.orEmpty(), style = MaterialTheme.typography.bodyMedium)
        state.contestationAdverse?.commentaire?.let {
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(text = "« $it »", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(PDZSpacing.space4))
        Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space3)) {
            PDZButton(
                text = "Refuser",
                type = PDZButtonType.Secondary,
                onClick = { onAction(ResultatAction.OnRefuserCorrectionClick) },
                modifier = Modifier.weight(1f),
            )
            PDZButton(
                text = "Accepter",
                isLoading = state.isActionLoading,
                onClick = { onAction(ResultatAction.OnAccepterCorrectionClick) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private fun Evenement.toTimelineEntry(campAId: String?): TimelineEntry {
    val titre = when (type) {
        TypeEvenement.BUT -> joueurPseudo.orEmpty()
        TypeEvenement.BUT_CONTRE_SON_CAMP -> "${joueurPseudo.orEmpty()} (csc)"
        TypeEvenement.PENALTY_MARQUE -> "${joueurPseudo.orEmpty()} (penalty)"
        TypeEvenement.PENALTY_MANQUE -> "${joueurPseudo.orEmpty()} (penalty manqué)"
        TypeEvenement.ARRET_DECISIF -> "${joueurPseudo.orEmpty()} (arrêt décisif)"
        TypeEvenement.CARTON -> "${joueurPseudo.orEmpty()} (carton)"
        TypeEvenement.REMPLACEMENT -> "${joueurPseudo.orEmpty()} ➜ ${joueurSecondairePseudo.orEmpty()}"
        TypeEvenement.DEBUT_PERIODE, TypeEvenement.FIN_PERIODE -> type.name
    }
    val sousTitre = joueurSecondairePseudo?.takeIf { type == TypeEvenement.BUT }?.let { "passe de $it" }
    return TimelineEntry(
        id = id,
        minute = minute,
        title = titre,
        subtitle = sousTitre,
        side = if (campId == campAId) TimelineSide.A else TimelineSide.B,
    )
}
