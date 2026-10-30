package empire.digiprem.projet2_0.feature.deuxzero.engager_club

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EngagerClubRoot(
    onEngaged: (camp: Camp) -> Unit,
    viewModel: EngagerClubViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            is EngagerClubEvent.OnEngaged -> onEngaged(event.camp)
        }
    }

    EngagerClubScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun EngagerClubScreen(
    state: EngagerClubState,
    onAction: (EngagerClubAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = PDZSpacing.space4)) {
            Spacer(Modifier.height(PDZSpacing.space8))
            Text(text = "Engager mon club", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(
                text = "L'organisateur du 2-0 t'a transmis un code d'engagement pour un camp.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(PDZSpacing.space6))

            PDZTextField(
                value = state.code,
                onValueChange = { onAction(EngagerClubAction.OnCodeChange(it)) },
                label = "Code d'engagement",
                placeholder = "Ex. A7K2B3C9",
                supportingText = state.errorMessage?.asString(),
                isError = state.errorMessage != null,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(PDZSpacing.space6))
            PDZButton(
                text = "Engager le club",
                onClick = { onAction(EngagerClubAction.OnSubmit) },
                isLoading = state.isLoading,
                enabled = state.code.isNotBlank(),
            )
        }
    }
}
