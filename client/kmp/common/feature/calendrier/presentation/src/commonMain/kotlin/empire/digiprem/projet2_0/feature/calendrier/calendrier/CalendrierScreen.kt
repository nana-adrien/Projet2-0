package empire.digiprem.projet2_0.feature.calendrier.calendrier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import org.koin.compose.viewmodel.koinViewModel

private val STATUT_LABEL = mapOf(
    "PROGRAMME" to "Programmé",
    "REPORTE" to "Reporté",
    "ANNULE" to "Annulé",
    "EN_COURS" to "En cours",
    "EN_ATTENTE_DE_VALIDATION" to "En attente de validation",
    "OFFICIEL" to "Officiel",
    "EN_LITIGE" to "En litige",
)

@Composable
fun CalendrierRoot(
    deuxZeroId: String,
    onMatchClick: (match: Match) -> Unit,
    onParametresClick: (deuxZeroId: String) -> Unit = {},
    viewModel: CalendrierViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(deuxZeroId) { viewModel.onAction(CalendrierAction.OnInit(deuxZeroId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            is CalendrierEvent.OnNavigateToFicheMatch -> onMatchClick(event.match)
            is CalendrierEvent.OnNavigateToParametres -> onParametresClick(event.deuxZeroId)
        }
    }

    CalendrierScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun CalendrierScreen(
    state: CalendrierState,
    onAction: (CalendrierAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        when {
            state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())

            else -> Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(PDZSpacing.space4),
                    horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2),
                ) {
                    Text(text = "Matchs", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                    if (state.peutModifierParametres) {
                        PDZButton(
                            text = "Paramètres",
                            type = PDZButtonType.Text,
                            fullWidth = false,
                            onClick = { onAction(CalendrierAction.OnParametresClick) },
                        )
                    }
                    PDZButton(
                        text = "+",
                        type = PDZButtonType.Secondary,
                        fullWidth = false,
                        onClick = { onAction(CalendrierAction.OnAjouterMatchClick) },
                    )
                }

                Row(
                    modifier = Modifier.padding(horizontal = PDZSpacing.space4),
                    horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2),
                ) {
                    FilterChip(
                        selected = state.filtre == CalendrierFiltre.A_VENIR,
                        onClick = { onAction(CalendrierAction.OnFiltreChange(CalendrierFiltre.A_VENIR)) },
                        label = { Text("À venir") },
                    )
                    FilterChip(
                        selected = state.filtre == CalendrierFiltre.PASSES,
                        onClick = { onAction(CalendrierAction.OnFiltreChange(CalendrierFiltre.PASSES)) },
                        label = { Text("Passés") },
                    )
                }

                PDZButton(
                    text = "Générer le calendrier",
                    type = PDZButtonType.Text,
                    isLoading = state.isGenerating,
                    onClick = { onAction(CalendrierAction.OnGenererCalendrierClick) },
                    modifier = Modifier.padding(horizontal = PDZSpacing.space4),
                )

                state.errorMessage?.let {
                    Text(
                        text = it.asString(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = PDZSpacing.space4),
                    )
                }

                if (state.matchsFiltres.isEmpty()) {
                    Text(
                        text = "Aucun match pour l'instant.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(PDZSpacing.space4),
                    )
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(state.matchsFiltres) { match ->
                            MatchRow(match = match, onClick = { onAction(CalendrierAction.OnMatchClick(match)) })
                        }
                    }
                }
            }
        }
    }

    if (state.showAjouterMatchDialog) {
        AjouterMatchDialog(
            onDismiss = { onAction(CalendrierAction.OnDismissAjouterMatchDialog) },
            onConfirm = { date, heure, lieu -> onAction(CalendrierAction.OnAjouterMatchConfirm(date, heure, lieu)) },
        )
    }
}

@Composable
private fun MatchRow(match: Match, onClick: () -> Unit) {
    val sousTitre = buildString {
        append(match.heure)
        match.lieu?.let { append(" · $it") }
        append(" · ${STATUT_LABEL[match.statut] ?: match.statut}")
    }
    AppListRow(title = match.date, subtitle = sousTitre, onClick = onClick)
}

@Composable
private fun AjouterMatchDialog(onDismiss: () -> Unit, onConfirm: (date: String, heure: String, lieu: String?) -> Unit) {
    var date by remember { mutableStateOf("") }
    var heure by remember { mutableStateOf("") }
    var lieu by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Match exceptionnel") },
        text = {
            Column {
                PDZTextField(value = date, onValueChange = { date = it }, label = "Date", placeholder = "AAAA-MM-JJ", modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(PDZSpacing.space3))
                PDZTextField(value = heure, onValueChange = { heure = it }, label = "Heure", placeholder = "HH:mm", modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(PDZSpacing.space3))
                PDZTextField(value = lieu, onValueChange = { lieu = it }, label = "Lieu (facultatif)", modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(date, heure, lieu.ifBlank { null }) },
                enabled = date.isNotBlank() && heure.isNotBlank(),
            ) { Text("Ajouter") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annuler") } },
    )
}
