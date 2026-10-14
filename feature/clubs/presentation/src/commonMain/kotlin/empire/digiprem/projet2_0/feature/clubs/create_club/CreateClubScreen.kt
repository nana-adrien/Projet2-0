package empire.digiprem.projet2_0.feature.clubs.create_club

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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateClubRoot(
    onCreated: (clubId: String) -> Unit,
    viewModel: CreateClubViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            is CreateClubEvent.OnCreated -> onCreated(event.clubId)
        }
    }

    CreateClubScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun CreateClubScreen(
    state: CreateClubState,
    onAction: (CreateClubAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = PDZSpacing.space4)) {
            Spacer(Modifier.height(PDZSpacing.space8))
            Text(text = "Nouveau club", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(PDZSpacing.space6))

            PDZTextField(
                value = state.nom,
                onValueChange = { onAction(CreateClubAction.OnNomChange(it)) },
                label = "Nom du club",
                placeholder = "Ex. Bel-Air",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))
            PDZTextField(
                value = state.quartier,
                onValueChange = { onAction(CreateClubAction.OnQuartierChange(it)) },
                label = "Quartier (facultatif)",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))
            PDZTextField(
                value = state.description,
                onValueChange = { onAction(CreateClubAction.OnDescriptionChange(it)) },
                label = "Description (facultative)",
                modifier = Modifier.fillMaxWidth(),
            )

            state.errorMessage?.let {
                Spacer(Modifier.height(PDZSpacing.space3))
                Text(text = it.asString(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(PDZSpacing.space6))
            PDZButton(
                text = "Créer le club",
                onClick = { onAction(CreateClubAction.OnSubmit) },
                isLoading = state.isLoading,
                enabled = state.nom.isNotBlank(),
            )
        }
    }
}
