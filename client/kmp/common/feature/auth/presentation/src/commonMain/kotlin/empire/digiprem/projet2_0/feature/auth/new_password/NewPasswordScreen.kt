package empire.digiprem.projet2_0.feature.auth.new_password

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZPasswordTextField
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewPasswordRoot(
    contact: String,
    verificationToken: String,
    onPasswordResetSuccess: () -> Unit,
    viewModel: NewPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(contact, verificationToken) {
        viewModel.onAction(NewPasswordAction.OnInit(contact, verificationToken))
    }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            NewPasswordEvent.OnPasswordResetSuccess -> onPasswordResetSuccess()
        }
    }

    NewPasswordScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun NewPasswordScreen(
    state: NewPasswordState,
    onAction: (NewPasswordAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = PDZSpacing.space4),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(Modifier.height(PDZSpacing.space8))
            Text(text = "Choisis un nouveau mot de passe", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(PDZSpacing.space6))

            PDZPasswordTextField(
                value = state.newPassword,
                onValueChange = { onAction(NewPasswordAction.OnPasswordChange(it)) },
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibility = { onAction(NewPasswordAction.OnToggleVisibility) },
                label = "Nouveau mot de passe",
                supportingText = "Au moins 8 caractères.",
                isError = state.errorMessage != null,
                modifier = Modifier.fillMaxWidth(),
            )

            state.errorMessage?.let {
                Spacer(Modifier.height(PDZSpacing.space2))
                Text(
                    text = it.asString(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(PDZSpacing.space6))
            PDZButton(
                text = "Valider",
                onClick = { onAction(NewPasswordAction.OnSubmit) },
                isLoading = state.isLoading,
                enabled = state.isFormValid,
            )
        }
    }
}
