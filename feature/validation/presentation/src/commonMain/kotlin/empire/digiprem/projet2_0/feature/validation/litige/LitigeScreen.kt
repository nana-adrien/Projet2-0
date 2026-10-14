package empire.digiprem.projet2_0.feature.validation.litige

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.feedback.PDZToast
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.timeline.Timeline
import empire.digiprem.projet2_0.core.design_system.components.timeline.TimelineEntry
import empire.digiprem.projet2_0.core.design_system.components.timeline.TimelineSide
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenement
import empire.digiprem.projet2_0.feature.validation.model.DecisionLitige
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LitigeRoot(
    matchId: String,
    deuxZeroId: String,
    onTranche: () -> Unit,
    viewModel: LitigeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(matchId) { viewModel.onAction(LitigeAction.OnInit(matchId, deuxZeroId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            LitigeEvent.OnTranche -> onTranche()
        }
    }

    LitigeScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun LitigeScreen(
    state: LitigeState,
    onAction: (LitigeAction) -> Unit,
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
                        Box(modifier = Modifier.fillMaxWidth().background(PDZColor.Erreur).padding(PDZSpacing.space3)) {
                            Text(
                                text = "Les capitaines ne sont pas d'accord. Ta décision sera définitive.",
                                color = PDZColor.Blanc,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Spacer(Modifier.height(PDZSpacing.space4))
                    }

                    item {
                        Text(text = "Résultat saisi", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(PDZSpacing.space2))
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

                    state.correctionProposee?.let { correction ->
                        item {
                            VersionCard(titre = "Correction proposée par ${correction.signatairePseudo}", corps = correction.correctionProposee.orEmpty())
                            Spacer(Modifier.height(PDZSpacing.space3))
                        }
                    }

                    state.reponseDeRefus?.let { reponse ->
                        item {
                            VersionCard(titre = "Réponse de ${reponse.signatairePseudo}", corps = "Refusée : « ${reponse.commentaire.orEmpty()} »")
                            Spacer(Modifier.height(PDZSpacing.space6))
                        }
                    }

                    item {
                        Text(text = "Ta décision", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(PDZSpacing.space2))
                        PDZButton(
                            text = "Garder le résultat saisi",
                            type = PDZButtonType.Secondary,
                            onClick = { onAction(LitigeAction.OnDecisionClick(DecisionLitige.GARDER_RESULTAT_SAISI)) },
                        )
                        Spacer(Modifier.height(PDZSpacing.space2))
                        PDZButton(
                            text = "Appliquer la correction",
                            onClick = { onAction(LitigeAction.OnDecisionClick(DecisionLitige.APPLIQUER_CORRECTION)) },
                        )
                        Spacer(Modifier.height(PDZSpacing.space2))
                        PDZButton(
                            text = "Annuler le match",
                            type = PDZButtonType.Text,
                            onClick = { onAction(LitigeAction.OnDecisionClick(DecisionLitige.ANNULER_MATCH)) },
                        )
                    }
                }
            }
        }

        state.message?.let { message ->
            LaunchedEffect(message) {
                delay(3_000)
                onAction(LitigeAction.OnMessageShown)
            }
            PDZToast(message = message.asString(), modifier = Modifier.align(Alignment.BottomCenter).padding(PDZSpacing.space4))
        }
    }

    if (state.showMotifDialog) {
        var motif by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { onAction(LitigeAction.OnDismissMotifDialog) },
            title = { Text("Confirmer la décision") },
            text = {
                Column {
                    Text("Cette décision est définitive. Indique un motif court.")
                    Spacer(Modifier.height(PDZSpacing.space3))
                    OutlinedTextField(value = motif, onValueChange = { motif = it }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(enabled = motif.isNotBlank(), onClick = { onAction(LitigeAction.OnConfirmerDecision(motif)) }) { Text("Confirmer") }
            },
            dismissButton = { TextButton(onClick = { onAction(LitigeAction.OnDismissMotifDialog) }) { Text("Annuler") } },
        )
    }
}

@Composable
private fun VersionCard(titre: String, corps: String) {
    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(PDZSpacing.space3),
    ) {
        Text(text = titre, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(4.dp))
        Text(text = corps, style = MaterialTheme.typography.bodyMedium)
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
