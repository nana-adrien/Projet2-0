package empire.digiprem.projet2_0.feature.amical.creer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.components.topbar.PDZTopBar
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreerAmicalRoot(
    clubId: String,
    onNavigateBack: () -> Unit,
    onCree: (matchId: String) -> Unit,
    viewModel: CreerAmicalViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(clubId) { viewModel.onAction(CreerAmicalAction.OnInit(clubId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            is CreerAmicalEvent.OnCree -> onCree(event.matchId)
        }
    }

    CreerAmicalScreen(state = state, onAction = viewModel::onAction, onNavigateBack = onNavigateBack)
}

@Composable
fun CreerAmicalScreen(
    state: CreerAmicalState,
    onAction: (CreerAmicalAction) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { PDZTopBar(title = "Match amical", onNavigateBack = onNavigateBack) },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(PDZSpacing.space4)) {
            Row(
                modifier = Modifier.fillMaxWidth().background(PDZColor.AlerteFond, RoundedCornerShape(8.dp)).padding(PDZSpacing.space3),
            ) {
                Text(
                    text = "Un amical ne compte pas dans la saison du 2-0. Les stats sont rangées à part.",
                    style = MaterialTheme.typography.bodySmall,
                    color = PDZColor.AlerteTexte,
                )
            }
            Spacer(Modifier.height(PDZSpacing.space6))

            Text(text = "Adversaire", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(PDZSpacing.space2))
            PDZTextField(
                value = state.nomEquipeInvitee,
                onValueChange = { onAction(CreerAmicalAction.OnNomEquipeChange(it)) },
                label = "Nom de l'équipe",
                supportingText = "Elle n'a pas besoin de compte. Seuls tes buteurs seront nommés.",
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            PDZTextField(
                value = state.date,
                onValueChange = { onAction(CreerAmicalAction.OnDateChange(it)) },
                label = "Date (AAAA-MM-JJ)",
            )
            Spacer(Modifier.height(PDZSpacing.space3))
            PDZTextField(
                value = state.heure,
                onValueChange = { onAction(CreerAmicalAction.OnHeureChange(it)) },
                label = "Heure (HH:mm)",
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            Text(text = "Mi-temps", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(PDZSpacing.space2))
            Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
                PDZButton(
                    text = "45 min",
                    type = if (state.dureeMiTemps == DureeMiTemps.QUARANTE_CINQ) PDZButtonType.Primary else PDZButtonType.Secondary,
                    onClick = { onAction(CreerAmicalAction.OnDureeMiTempsChange(DureeMiTemps.QUARANTE_CINQ)) },
                    modifier = Modifier.weight(1f),
                )
                PDZButton(
                    text = "30 min",
                    type = if (state.dureeMiTemps == DureeMiTemps.TRENTE) PDZButtonType.Primary else PDZButtonType.Secondary,
                    onClick = { onAction(CreerAmicalAction.OnDureeMiTempsChange(DureeMiTemps.TRENTE)) },
                    modifier = Modifier.weight(1f),
                )
                PDZButton(
                    text = "Libre",
                    type = if (state.dureeMiTemps == DureeMiTemps.LIBRE) PDZButtonType.Primary else PDZButtonType.Secondary,
                    onClick = { onAction(CreerAmicalAction.OnDureeMiTempsChange(DureeMiTemps.LIBRE)) },
                    modifier = Modifier.weight(1f),
                )
            }

            state.errorMessage?.let {
                Spacer(Modifier.height(PDZSpacing.space3))
                Text(text = it.asString(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(PDZSpacing.space6))
            PDZButton(
                text = "Créer et convoquer",
                enabled = state.peutCreer,
                isLoading = state.isLoading,
                onClick = { onAction(CreerAmicalAction.OnCreerEtConvoquerClick) },
            )
        }
    }
}
