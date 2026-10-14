package empire.digiprem.projet2_0.feature.deuxzero.composer_effectif

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
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
fun ComposerEffectifRoot(
    camp: Camp,
    clubId: String,
    onDone: () -> Unit,
    viewModel: ComposerEffectifViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(camp.id, clubId) { viewModel.onAction(ComposerEffectifAction.OnInit(camp, clubId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            ComposerEffectifEvent.OnDone -> onDone()
        }
    }

    ComposerEffectifScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun ComposerEffectifScreen(
    state: ComposerEffectifState,
    onAction: (ComposerEffectifAction) -> Unit,
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
                        Text(text = "Effectif — ${state.camp?.libelle.orEmpty()}", style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(PDZSpacing.space6))
                    }

                    item { Text(text = "Effectif (${state.effectif.size})", style = MaterialTheme.typography.titleMedium) }
                    items(state.effectif) { effectif ->
                        EffectifRow(
                            effectif = effectif,
                            camp = state.camp,
                            onNameCaptain = { onAction(ComposerEffectifAction.OnNameCaptain(effectif.utilisateurId, false)) },
                            onNameViceCaptain = { onAction(ComposerEffectifAction.OnNameCaptain(effectif.utilisateurId, true)) },
                        )
                    }

                    item { Spacer(Modifier.height(PDZSpacing.space4)) }
                    item { Text(text = "Ajouter un membre du club", style = MaterialTheme.typography.titleMedium) }
                    items(state.membresDisponibles) { membre ->
                        AppListRow(
                            title = membre.pseudo,
                            leading = { AppAvatar(initials = membre.pseudo) },
                            trailing = {
                                IconButton(onClick = { onAction(ComposerEffectifAction.OnAddMember(membre.utilisateurId)) }) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = "Ajouter")
                                }
                            },
                        )
                    }

                    item { Spacer(Modifier.height(PDZSpacing.space6)) }
                    item { PDZButton(text = "Terminer", onClick = { onAction(ComposerEffectifAction.OnDoneClick) }) }
                }
            }
        }

        state.message?.let { message ->
            LaunchedEffect(message) {
                delay(3_000)
                onAction(ComposerEffectifAction.OnMessageShown)
            }
            PDZToast(
                message = message.asString(),
                modifier = Modifier.align(Alignment.BottomCenter).padding(PDZSpacing.space4),
            )
        }
    }
}

@Composable
private fun EffectifRow(
    effectif: Effectif,
    camp: Camp?,
    onNameCaptain: () -> Unit,
    onNameViceCaptain: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val estCapitaine = camp?.capitaineId == effectif.utilisateurId
    val estViceCapitaine = camp?.viceCapitaineId == effectif.utilisateurId
    val sousTitre = when {
        estCapitaine -> "Capitaine"
        estViceCapitaine -> "Vice-capitaine"
        effectif.numeroMaillot != null -> "N° ${effectif.numeroMaillot}"
        else -> null
    }

    AppListRow(
        title = effectif.pseudo,
        subtitle = sousTitre,
        leading = { AppAvatar(initials = effectif.pseudo) },
        trailing = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Actions")
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(text = { Text("Nommer capitaine") }, onClick = { menuExpanded = false; onNameCaptain() })
                    DropdownMenuItem(text = { Text("Nommer vice-capitaine") }, onClick = { menuExpanded = false; onNameViceCaptain() })
                }
            }
        },
    )
}
