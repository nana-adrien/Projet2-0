package empire.digiprem.projet2_0.feature.dashboard.accueil

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.states.PDZStateContent
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccueilRoot(
    onMatchClick: (match: Match) -> Unit,
    viewModel: AccueilViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.onAction(AccueilAction.OnLoad) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            is AccueilEvent.OnNavigateToFicheMatch -> onMatchClick(event.match)
        }
    }

    AccueilScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccueilScreen(
    state: AccueilState,
    onAction: (AccueilAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        when {
            state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())

            state.deuxZeroActuel == null -> Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                PDZStateContent(
                    icon = Icons.Default.Home,
                    title = "Pas encore de 2-0",
                    description = "Rejoins ou crée un 2-0 depuis l'onglet Clubs pour voir ton prochain match ici.",
                    modifier = Modifier.weight(1f),
                )
            }

            else -> Column(modifier = Modifier.padding(padding).fillMaxSize().padding(PDZSpacing.space4)) {
                DeuxZeroHeader(deuxZero = state.deuxZeroActuel, onClick = { onAction(AccueilAction.OnChangerDeuxZeroClick) })
                Spacer(Modifier.height(PDZSpacing.space6))

                state.errorMessage?.let {
                    Text(text = it.asString(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(PDZSpacing.space4))
                }

                if (state.prochainMatch == null) {
                    Text(
                        text = "Aucun match à venir pour l'instant.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    ProchainMatchCard(state = state, onAction = onAction)
                }
            }
        }
    }

    if (state.showChangerDeuxZero) {
        ModalBottomSheet(onDismissRequest = { onAction(AccueilAction.OnDismissChangerDeuxZero) }) {
            Column(modifier = Modifier.padding(PDZSpacing.space4)) {
                Text(text = "Mes 2-0", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(PDZSpacing.space2))
                LazyColumn {
                    items(state.mesDeuxZero) { deuxZero ->
                        AppListRow(
                            title = deuxZero.nom,
                            subtitle = deuxZero.lieu,
                            leading = { AppAvatar(initials = deuxZero.nom) },
                            onClick = { onAction(AccueilAction.OnSelectDeuxZero(deuxZero)) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeuxZeroHeader(deuxZero: DeuxZero, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space3),
    ) {
        AppAvatar(initials = deuxZero.nom)
        Column(modifier = Modifier.weight(1f)) {
            Text(text = deuxZero.nom, style = MaterialTheme.typography.titleMedium)
            deuxZero.lieu?.let { Text(text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
        PDZButton(text = "Changer", type = PDZButtonType.Text, fullWidth = false, onClick = onClick)
    }
}

@Composable
private fun ProchainMatchCard(state: AccueilState, onAction: (AccueilAction) -> Unit) {
    val match = state.prochainMatch ?: return
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(PDZSpacing.space4)) {
            Text(text = "Prochain match", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(
                text = buildString {
                    append(match.date)
                    append(" · ")
                    append(match.heure)
                    match.lieu?.let { append(" · $it") }
                },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable(onClick = { onAction(AccueilAction.OnMatchClick(match)) }),
            )
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(
                text = "${state.nombrePresents} présent${if (state.nombrePresents > 1) "s" else ""} sur ${state.convocations.size}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            state.maConvocation?.let { maConvocation ->
                Spacer(Modifier.height(PDZSpacing.space4))
                Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
                    PDZButton(
                        text = "Présent",
                        type = if (maConvocation.reponse == ReponseConvocation.PRESENT) PDZButtonType.Primary else PDZButtonType.Secondary,
                        fullWidth = false,
                        onClick = { onAction(AccueilAction.OnRepondre(ReponseConvocation.PRESENT)) },
                        modifier = Modifier.weight(1f),
                    )
                    PDZButton(
                        text = "Incertain",
                        type = if (maConvocation.reponse == ReponseConvocation.INCERTAIN) PDZButtonType.Primary else PDZButtonType.Secondary,
                        fullWidth = false,
                        onClick = { onAction(AccueilAction.OnRepondre(ReponseConvocation.INCERTAIN)) },
                        modifier = Modifier.weight(1f),
                    )
                    PDZButton(
                        text = "Absent",
                        type = if (maConvocation.reponse == ReponseConvocation.ABSENT) PDZButtonType.Primary else PDZButtonType.Secondary,
                        fullWidth = false,
                        onClick = { onAction(AccueilAction.OnRepondre(ReponseConvocation.ABSENT)) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}
