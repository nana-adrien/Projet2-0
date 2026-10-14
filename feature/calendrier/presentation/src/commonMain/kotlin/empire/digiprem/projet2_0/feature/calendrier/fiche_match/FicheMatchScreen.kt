package empire.digiprem.projet2_0.feature.calendrier.fiche_match

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.feedback.PDZToast
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.calendrier.model.StatutMatchModifiable
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FicheMatchRoot(
    match: Match,
    deuxZeroId: String,
    onComposerEquipe: (camp: Camp, matchId: String, deuxZeroId: String) -> Unit = { _, _, _ -> },
    onMarqueur: (matchId: String, deuxZeroId: String) -> Unit = { _, _ -> },
    onSuivreEnDirect: (matchId: String, deuxZeroId: String) -> Unit = { _, _ -> },
    viewModel: FicheMatchViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(match.id, deuxZeroId) { viewModel.onAction(FicheMatchAction.OnInit(match, deuxZeroId)) }

    FicheMatchScreen(
        state = state,
        onAction = viewModel::onAction,
        onComposerEquipe = onComposerEquipe,
        onMarqueur = onMarqueur,
        onSuivreEnDirect = onSuivreEnDirect,
    )
}

@Composable
fun FicheMatchScreen(
    state: FicheMatchState,
    onAction: (FicheMatchAction) -> Unit,
    modifier: Modifier = Modifier,
    onComposerEquipe: (camp: Camp, matchId: String, deuxZeroId: String) -> Unit = { _, _, _ -> },
    onMarqueur: (matchId: String, deuxZeroId: String) -> Unit = { _, _ -> },
    onSuivreEnDirect: (matchId: String, deuxZeroId: String) -> Unit = { _, _ -> },
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold { padding ->
            when {
                state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())

                else -> LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = PaddingValues(PDZSpacing.space4),
                ) {
                    item { MatchHeader(match = state.match) }
                    item { Spacer(Modifier.height(PDZSpacing.space4)) }

                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space3)) {
                            PDZButton(
                                text = "Générer les convocations",
                                type = PDZButtonType.Secondary,
                                isLoading = state.isActionLoading,
                                onClick = { onAction(FicheMatchAction.OnGenererConvocationsClick) },
                                modifier = Modifier.weight(1f),
                            )
                            PDZButton(
                                text = "Relancer",
                                type = PDZButtonType.Secondary,
                                onClick = { onAction(FicheMatchAction.OnRelancerClick) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        Spacer(Modifier.height(PDZSpacing.space2))
                        PDZButton(
                            text = "Reporter / annuler",
                            type = PDZButtonType.Text,
                            onClick = { onAction(FicheMatchAction.OnShowModifierDialog) },
                        )
                        Spacer(Modifier.height(PDZSpacing.space4))
                    }

                    if (state.monCamp != null || state.estMarqueurOuSuppleant || state.peutSuivreEnDirect) {
                        item {
                            Text(text = "Match en direct", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(PDZSpacing.space2))
                            state.monCamp?.let { camp ->
                                PDZButton(
                                    text = "Composer l'équipe des ${camp.libelle}",
                                    type = PDZButtonType.Secondary,
                                    onClick = { state.match?.let { onComposerEquipe(camp, it.id, state.deuxZeroId) } },
                                )
                                Spacer(Modifier.height(PDZSpacing.space2))
                            }
                            if (state.estMarqueurOuSuppleant) {
                                PDZButton(
                                    text = "Marqueur du match",
                                    type = PDZButtonType.Secondary,
                                    onClick = { state.match?.let { onMarqueur(it.id, state.deuxZeroId) } },
                                )
                                Spacer(Modifier.height(PDZSpacing.space2))
                            }
                            if (state.peutSuivreEnDirect) {
                                PDZButton(
                                    text = "Suivre en direct",
                                    type = PDZButtonType.Secondary,
                                    onClick = { state.match?.let { onSuivreEnDirect(it.id, state.deuxZeroId) } },
                                )
                            }
                            Spacer(Modifier.height(PDZSpacing.space4))
                        }
                    }

                    state.maConvocation?.let { maConvocation ->
                        item {
                            Text(text = "Ta réponse", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(PDZSpacing.space2))
                            Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
                                PDZButton(
                                    text = "Présent",
                                    type = if (maConvocation.reponse == ReponseConvocation.PRESENT) PDZButtonType.Primary else PDZButtonType.Secondary,
                                    onClick = { onAction(FicheMatchAction.OnRepondre(ReponseConvocation.PRESENT)) },
                                    fullWidth = false,
                                    modifier = Modifier.weight(1f),
                                )
                                PDZButton(
                                    text = "Incertain",
                                    type = if (maConvocation.reponse == ReponseConvocation.INCERTAIN) PDZButtonType.Primary else PDZButtonType.Secondary,
                                    onClick = { onAction(FicheMatchAction.OnRepondre(ReponseConvocation.INCERTAIN)) },
                                    fullWidth = false,
                                    modifier = Modifier.weight(1f),
                                )
                                PDZButton(
                                    text = "Absent",
                                    type = if (maConvocation.reponse == ReponseConvocation.ABSENT) PDZButtonType.Primary else PDZButtonType.Secondary,
                                    onClick = { onAction(FicheMatchAction.OnRepondre(ReponseConvocation.ABSENT)) },
                                    fullWidth = false,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            Spacer(Modifier.height(PDZSpacing.space4))
                        }
                    }

                    item {
                        Text(text = "Présences — ${state.nombrePresents} sur ${state.convocations.size}", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(PDZSpacing.space2))
                        PresenceBar(convocations = state.convocations)
                        Spacer(Modifier.height(PDZSpacing.space4))
                    }

                    items(state.convocationsTriees) { convocation ->
                        ConvocationRow(convocation = convocation)
                    }
                }
            }
        }

        state.message?.let { message ->
            LaunchedEffect(message) {
                delay(3_000)
                onAction(FicheMatchAction.OnMessageShown)
            }
            PDZToast(message = message.asString(), modifier = Modifier.align(Alignment.BottomCenter).padding(PDZSpacing.space4))
        }
    }

    if (state.showModifierDialog) {
        ModifierMatchDialog(
            onDismiss = { onAction(FicheMatchAction.OnDismissModifierDialog) },
            onConfirm = { statut, date, heure, motif -> onAction(FicheMatchAction.OnModifierConfirm(statut, date, heure, motif)) },
        )
    }
}

@Composable
private fun MatchHeader(match: Match?) {
    if (match == null) return
    Column {
        Text(text = match.date, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = buildString {
                append(match.heure)
                match.lieu?.let { append(" · $it") }
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun PresenceBar(convocations: List<Convocation>) {
    val total = convocations.size.coerceAtLeast(1)
    val presents = convocations.count { it.reponse == ReponseConvocation.PRESENT }
    val incertains = convocations.count { it.reponse == ReponseConvocation.INCERTAIN }
    val sansReponse = convocations.count { it.reponse == ReponseConvocation.SANS_REPONSE }

    Row(modifier = Modifier.fillMaxWidth().height(8.dp)) {
        Segment(weight = presents.toFloat() / total, color = PDZColor.Succes)
        Segment(weight = incertains.toFloat() / total, color = PDZColor.Alerte)
        Segment(weight = sansReponse.toFloat() / total, color = PDZColor.Gris300)
    }
}

@Composable
private fun Segment(weight: Float, color: Color) {
    if (weight <= 0f) return
    Box(modifier = Modifier.fillMaxWidth(weight).height(8.dp).background(color))
}

@Composable
private fun ConvocationRow(convocation: Convocation) {
    val sousTitre = when (convocation.reponse) {
        ReponseConvocation.PRESENT      -> "Présent"
        ReponseConvocation.ABSENT       -> "Absent"
        ReponseConvocation.INCERTAIN    -> "Incertain"
        ReponseConvocation.SANS_REPONSE -> "Sans réponse"
    }
    AppListRow(
        title = convocation.pseudo,
        subtitle = sousTitre,
        leading = { AppAvatar(initials = convocation.pseudo) },
    )
}

@Composable
private fun ModifierMatchDialog(
    onDismiss: () -> Unit,
    onConfirm: (statut: StatutMatchModifiable, date: String?, heure: String?, motif: String?) -> Unit,
) {
    var statut by remember { mutableStateOf(StatutMatchModifiable.REPORTE) }
    var motif by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reporter ou annuler le match") },
        text = {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
                    PDZButton(
                        text = "Reporter",
                        type = if (statut == StatutMatchModifiable.REPORTE) PDZButtonType.Primary else PDZButtonType.Secondary,
                        onClick = { statut = StatutMatchModifiable.REPORTE },
                        fullWidth = false,
                        modifier = Modifier.weight(1f),
                    )
                    PDZButton(
                        text = "Annuler",
                        type = if (statut == StatutMatchModifiable.ANNULE) PDZButtonType.Primary else PDZButtonType.Secondary,
                        onClick = { statut = StatutMatchModifiable.ANNULE },
                        fullWidth = false,
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(Modifier.height(PDZSpacing.space3))
                OutlinedTextField(
                    value = motif,
                    onValueChange = { motif = it },
                    label = { Text("Motif (obligatoire)") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(statut, null, null, motif) },
                enabled = motif.isNotBlank(),
            ) { Text("Confirmer") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annuler") } },
    )
}
