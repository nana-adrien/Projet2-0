package empire.digiprem.projet2_0.feature.matchendirect.composition

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.feedback.PDZToast
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CompositionRoot(
    camp: Camp,
    matchId: String,
    onDone: () -> Unit,
    viewModel: CompositionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(camp.id, matchId) { viewModel.onAction(CompositionAction.OnInit(camp, matchId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            CompositionEvent.OnDone -> onDone()
        }
    }

    CompositionScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompositionScreen(
    state: CompositionState,
    onAction: (CompositionAction) -> Unit,
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
                        Text(text = "Composition — ${state.camp?.libelle.orEmpty()}", style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(PDZSpacing.space6))
                    }

                    item { Text(text = "Titulaires (${state.titulaires.size})", style = MaterialTheme.typography.titleMedium) }
                    items(state.joueursEligibles) { joueur ->
                        JoueurRow(
                            joueur = joueur,
                            titulaire = state.compositionDe(joueur.utilisateurId)?.titulaire,
                            onTitulaireClick = { onAction(CompositionAction.OnToggleTitulaire(joueur.utilisateurId)) },
                            onRemplacantClick = { onAction(CompositionAction.OnToggleRemplacant(joueur.utilisateurId)) },
                        )
                    }

                    item { Spacer(Modifier.height(PDZSpacing.space6)) }
                    item { Text(text = "Marqueur du match", style = MaterialTheme.typography.titleMedium) }
                    item {
                        AppListRow(
                            title = state.marqueurPseudo ?: "Non désigné",
                            subtitle = "Marqueur",
                            leading = { AppAvatar(initials = state.marqueurPseudo ?: "?") },
                            trailing = { PDZButton(text = "Changer", type = PDZButtonType.Text, fullWidth = false, onClick = { onAction(CompositionAction.OnChangerMarqueurClick) }) },
                        )
                    }
                    item {
                        AppListRow(
                            title = state.suppleantPseudo ?: "Non désigné",
                            subtitle = "Suppléant",
                            leading = { AppAvatar(initials = state.suppleantPseudo ?: "?") },
                            trailing = { PDZButton(text = "Changer", type = PDZButtonType.Text, fullWidth = false, onClick = { onAction(CompositionAction.OnChangerSuppleantClick) }) },
                        )
                    }

                    state.errorMessage?.let {
                        item { Text(text = it.asString(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                    }

                    item { Spacer(Modifier.height(PDZSpacing.space6)) }
                    item { PDZButton(text = "Terminé", onClick = { onAction(CompositionAction.OnDoneClick) }) }
                }
            }
        }

        state.message?.let { message ->
            LaunchedEffect(message) {
                delay(3_000)
                onAction(CompositionAction.OnMessageShown)
            }
            PDZToast(message = message.asString(), modifier = Modifier.align(Alignment.BottomCenter).padding(PDZSpacing.space4))
        }
    }

    if (state.showMarqueurPicker || state.showSuppleantPicker) {
        ModalBottomSheet(onDismissRequest = { onAction(CompositionAction.OnDismissPicker) }) {
            Column(modifier = Modifier.padding(PDZSpacing.space4)) {
                Text(text = if (state.showMarqueurPicker) "Choisir le marqueur" else "Choisir le suppléant", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(PDZSpacing.space2))
                LazyColumn {
                    items(state.effectif) { joueur ->
                        AppListRow(
                            title = joueur.pseudo,
                            leading = { AppAvatar(initials = joueur.pseudo) },
                            onClick = {
                                if (state.showMarqueurPicker) onAction(CompositionAction.OnSelectMarqueur(joueur.utilisateurId))
                                else onAction(CompositionAction.OnSelectSuppleant(joueur.utilisateurId))
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JoueurRow(joueur: Effectif, titulaire: Boolean?, onTitulaireClick: () -> Unit, onRemplacantClick: () -> Unit) {
    AppListRow(
        title = joueur.pseudo,
        leading = { AppAvatar(initials = joueur.pseudo) },
        trailing = {
            Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space1)) {
                PDZButton(
                    text = "Titulaire",
                    type = if (titulaire == true) PDZButtonType.Primary else PDZButtonType.Secondary,
                    fullWidth = false,
                    onClick = onTitulaireClick,
                )
                PDZButton(
                    text = "Remplaçant",
                    type = if (titulaire == false) PDZButtonType.Primary else PDZButtonType.Secondary,
                    fullWidth = false,
                    onClick = onRemplacantClick,
                )
            }
        },
    )
}
