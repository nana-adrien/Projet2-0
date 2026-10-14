package empire.digiprem.projet2_0.feature.clubs.join_club

import androidx.compose.foundation.layout.Arrangement
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
fun JoinClubRoot(
    onJoined: () -> Unit,
    viewModel: JoinClubViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            JoinClubEvent.OnJoined -> onJoined()
        }
    }

    JoinClubScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun JoinClubScreen(
    state: JoinClubState,
    onAction: (JoinClubAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = PDZSpacing.space4)) {
            Spacer(Modifier.height(PDZSpacing.space8))
            Text(text = "Rejoindre un club", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(
                text = "Ton capitaine te l'a envoyé ? Colle-le ici.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(PDZSpacing.space6))

            PDZTextField(
                value = state.code,
                onValueChange = { onAction(JoinClubAction.OnCodeChange(it)) },
                label = "Code de club",
                placeholder = "Ex. BELAIR-A7K2",
                supportingText = state.errorMessage?.asString(),
                isError = state.errorMessage != null,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(PDZSpacing.space6))
            PDZButton(
                text = "Rejoindre",
                onClick = { onAction(JoinClubAction.OnSubmit) },
                isLoading = state.isLoading,
                enabled = state.code.isNotBlank(),
            )
        }
    }
}
