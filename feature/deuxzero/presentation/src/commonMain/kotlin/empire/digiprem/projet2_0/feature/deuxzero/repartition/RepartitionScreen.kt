package empire.digiprem.projet2_0.feature.deuxzero.repartition

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RepartitionRoot(
    campA: Camp,
    campB: Camp,
    clubId: String,
    onDone: (campA: Camp, campB: Camp, clubId: String) -> Unit,
    viewModel: RepartitionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(campA.id, campB.id, clubId) { viewModel.onAction(RepartitionAction.OnInit(campA, campB, clubId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            is RepartitionEvent.OnDone -> onDone(event.campA, event.campB, event.clubId)
        }
    }

    RepartitionScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun RepartitionScreen(
    state: RepartitionState,
    onAction: (RepartitionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        when {
            state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())

            else -> LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(PDZSpacing.space4),
            ) {
                item {
                    Text(text = "Répartir les ${state.membresAcceptes.size} membres", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(PDZSpacing.space4))
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space3)) {
                        CampSummary(
                            libelle = state.campA?.libelle.orEmpty(),
                            couleur = PDZColor.CampA,
                            nombreJoueurs = state.nombreCampA,
                            capitainePseudo = state.campA?.capitainePseudo,
                            modifier = Modifier.weight(1f),
                        )
                        CampSummary(
                            libelle = state.campB?.libelle.orEmpty(),
                            couleur = PDZColor.CampB,
                            nombreJoueurs = state.nombreCampB,
                            capitainePseudo = state.campB?.capitainePseudo,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Spacer(Modifier.height(PDZSpacing.space4))
                    PDZButton(
                        text = "Tirage équilibré automatique",
                        type = PDZButtonType.Secondary,
                        onClick = { onAction(RepartitionAction.OnTirageEquilibre) },
                    )
                    Spacer(Modifier.height(PDZSpacing.space4))
                }

                items(state.membresAcceptes) { membre ->
                    MembreRepartitionRow(
                        membre = membre,
                        side = state.assignments[membre.utilisateurId],
                        onAssign = { side -> onAction(RepartitionAction.OnAssign(membre.utilisateurId, side)) },
                    )
                }

                item { Spacer(Modifier.height(PDZSpacing.space2)) }
                item {
                    if (state.nombreNonRepartis > 0) {
                        Text(
                            text = "${state.nombreNonRepartis} membres non répartis restent disponibles comme remplaçants.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(PDZSpacing.space4))
                    }
                }

                state.errorMessage?.let {
                    item {
                        Text(text = it.asString(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(PDZSpacing.space2))
                    }
                }

                item {
                    PDZButton(
                        text = "Valider la répartition",
                        onClick = { onAction(RepartitionAction.OnValiderClick) },
                        enabled = state.peutValider,
                        isLoading = state.isSubmitting,
                    )
                }
            }
        }
    }
}

@Composable
private fun CampSummary(libelle: String, couleur: Color, nombreJoueurs: Int, capitainePseudo: String?, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.border(BorderStroke(4.dp, couleur), shape = MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(),
    ) {
        Column(modifier = Modifier.padding(PDZSpacing.space3)) {
            Text(text = libelle, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "$nombreJoueurs joueur${if (nombreJoueurs > 1) "s" else ""}" + (capitainePseudo?.let { ", capitaine $it" } ?: ""),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MembreRepartitionRow(membre: Membership, side: CampSide?, onAssign: (CampSide) -> Unit) {
    AppListRow(
        title = membre.pseudo,
        leading = { AppAvatar(initials = membre.pseudo, ringColor = if (side == CampSide.A) PDZColor.CampA else if (side == CampSide.B) PDZColor.CampB else null) },
        trailing = {
            Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space1)) {
                PDZButton(
                    text = "A",
                    type = if (side == CampSide.A) PDZButtonType.Primary else PDZButtonType.Secondary,
                    onClick = { onAssign(CampSide.A) },
                    fullWidth = false,
                )
                PDZButton(
                    text = "B",
                    type = if (side == CampSide.B) PDZButtonType.Primary else PDZButtonType.Secondary,
                    onClick = { onAssign(CampSide.B) },
                    fullWidth = false,
                )
            }
        },
    )
}
