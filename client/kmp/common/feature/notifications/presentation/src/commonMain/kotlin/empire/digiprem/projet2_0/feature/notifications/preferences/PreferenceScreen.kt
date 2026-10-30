package empire.digiprem.projet2_0.feature.notifications.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.feedback.PDZToast
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.topbar.PDZTopBar
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.feature.notifications.model.NiveauNotification
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PreferenceRoot(
    onNavigateBack: () -> Unit = {},
    viewModel: PreferenceViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.onAction(PreferenceAction.OnInit) }

    PreferenceScreen(state = state, onAction = viewModel::onAction, onNavigateBack = onNavigateBack)
}

@Composable
fun PreferenceScreen(
    state: PreferenceState,
    onAction: (PreferenceAction) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(topBar = { PDZTopBar(title = "Notifications", onNavigateBack = onNavigateBack) }) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                if (state.deuxZeros.size > 1) {
                    TabRow(selectedTabIndex = state.deuxZeros.indexOfFirst { it.id == state.deuxZeroSelectionneId }.coerceAtLeast(0)) {
                        state.deuxZeros.forEach { deuxZero ->
                            Tab(
                                selected = deuxZero.id == state.deuxZeroSelectionneId,
                                onClick = { onAction(PreferenceAction.OnDeuxZeroChange(deuxZero.id)) },
                                text = { Text(deuxZero.nom) },
                            )
                        }
                    }
                }

                if (state.isLoading) {
                    PDZLoadingSpinner(modifier = Modifier.fillMaxSize())
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(PDZSpacing.space4)) {
                        item { NiveauRow(label = "Tout", description = "Buts, mi-temps, score final, convocations", niveau = NiveauNotification.TOUT, state = state, onAction = onAction) }
                        item { NiveauRow(label = "Buts et score final", description = null, niveau = NiveauNotification.BUTS_ET_SCORE_FINAL, state = state, onAction = onAction) }
                        item { NiveauRow(label = "Seulement ce qui me concerne", description = "Convocations, validations, mes buts", niveau = NiveauNotification.ME_CONCERNANT, state = state, onAction = onAction) }
                        item { NiveauRow(label = "Aucune", description = null, niveau = NiveauNotification.AUCUNE, state = state, onAction = onAction) }

                        item {
                            Spacer(Modifier.height(PDZSpacing.space4))
                            AppListRow(
                                title = "Ne pas déranger",
                                subtitle = "De 23:00 à 8:00",
                                trailing = {
                                    Switch(
                                        checked = state.neRienDerangerNocturne,
                                        onCheckedChange = { onAction(PreferenceAction.OnNeRienDerangerChange(it)) },
                                    )
                                },
                            )
                        }

                        item {
                            Spacer(Modifier.height(PDZSpacing.space6))
                            Text(text = "Aperçu", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(PDZSpacing.space2))
                            val (titre, corps) = state.apercu
                            AppListRow(title = titre, subtitle = corps)
                            Spacer(Modifier.height(PDZSpacing.space6))
                            PDZButton(
                                text = "Enregistrer",
                                isLoading = state.isActionLoading,
                                onClick = { onAction(PreferenceAction.OnEnregistrerClick) },
                            )
                        }
                    }
                }
            }
        }

        state.message?.let { message ->
            LaunchedEffect(message) {
                delay(3_000)
                onAction(PreferenceAction.OnMessageShown)
            }
            PDZToast(message = message.asString(), modifier = Modifier.align(Alignment.BottomCenter).padding(PDZSpacing.space4))
        }
    }
}

@Composable
private fun NiveauRow(label: String, description: String?, niveau: NiveauNotification, state: PreferenceState, onAction: (PreferenceAction) -> Unit) {
    AppListRow(
        title = label,
        subtitle = description,
        onClick = { onAction(PreferenceAction.OnNiveauChange(niveau)) },
        trailing = { RadioButton(selected = state.niveau == niveau, onClick = { onAction(PreferenceAction.OnNiveauChange(niveau)) }) },
    )
}
