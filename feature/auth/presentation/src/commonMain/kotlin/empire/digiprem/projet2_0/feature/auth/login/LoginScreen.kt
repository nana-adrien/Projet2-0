package empire.digiprem.projet2_0.feature.auth.login

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZPasswordTextField
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginRoot(
    onLoginSuccess: () -> Unit,
    onOtpRequested: (contact: String, purpose: OtpPurpose) -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            LoginEvent.OnLoginSuccess -> onLoginSuccess()
            is LoginEvent.OnOtpRequested -> onOtpRequested(event.contact, event.purpose)
        }
    }

    LoginScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = PDZSpacing.space4),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Content de te revoir", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(PDZSpacing.space6))

            PDZTextField(
                value = state.contact,
                onValueChange = { onAction(LoginAction.OnContactChange(it)) },
                label = "Téléphone ou e-mail",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            PDZPasswordTextField(
                value = state.password,
                onValueChange = { onAction(LoginAction.OnPasswordChange(it)) },
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibility = { onAction(LoginAction.OnTogglePasswordVisibility) },
                label = "Mot de passe",
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

            Spacer(Modifier.height(PDZSpacing.space2))
            PDZButton(
                text = "Mot de passe oublié ?",
                onClick = { onAction(LoginAction.OnForgotPasswordClick) },
                type = PDZButtonType.Text,
                fullWidth = false,
            )

            Spacer(Modifier.height(PDZSpacing.space4))
            PDZButton(
                text = "Se connecter",
                onClick = { onAction(LoginAction.OnLoginClick) },
                isLoading = state.isLoading,
                enabled = state.isFormValid,
            )

            Spacer(Modifier.height(PDZSpacing.space3))
            PDZButton(
                text = "Recevoir un code de connexion",
                onClick = { onAction(LoginAction.OnLoginWithCodeClick) },
                type = PDZButtonType.Secondary,
                enabled = state.contact.isNotBlank(),
            )
        }
    }
}
