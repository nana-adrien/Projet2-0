package empire.digiprem.projet2_0.feature.auth.register_profile

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
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZPasswordTextField
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterProfileRoot(
    contact: String,
    verificationToken: String,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(contact, verificationToken) {
        viewModel.onAction(RegisterProfileAction.OnInit(contact, verificationToken))
    }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            RegisterProfileEvent.OnRegisterSuccess -> onRegisterSuccess()
        }
    }

    RegisterProfileScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun RegisterProfileScreen(
    state: RegisterProfileState,
    onAction: (RegisterProfileAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = PDZSpacing.space4),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(Modifier.height(PDZSpacing.space8))
            Text(text = "Comment on t'appelle ?", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(PDZSpacing.space6))

            PDZTextField(
                value = state.pseudo,
                onValueChange = { onAction(RegisterProfileAction.OnPseudoChange(it)) },
                label = "Pseudo",
                placeholder = "karim93",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            PDZPasswordTextField(
                value = state.password,
                onValueChange = { onAction(RegisterProfileAction.OnPasswordChange(it)) },
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibility = { onAction(RegisterProfileAction.OnTogglePasswordVisibility) },
                label = "Mot de passe (facultatif)",
                supportingText = "Laisse vide pour te connecter uniquement par code.",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            PDZTextField(
                value = state.clubCode,
                onValueChange = { onAction(RegisterProfileAction.OnClubCodeChange(it)) },
                label = "Code de club (facultatif)",
                placeholder = "Ex. BELAIR-A7K2",
                supportingText = "Ton capitaine te l'a envoyé ? Colle-le ici.",
                imeAction = ImeAction.Done,
                modifier = Modifier.fillMaxWidth(),
            )

            state.errorMessage?.let {
                Spacer(Modifier.height(PDZSpacing.space3))
                Text(
                    text = it.asString(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(PDZSpacing.space6))
            PDZButton(
                text = "C'est parti",
                onClick = { onAction(RegisterProfileAction.OnSubmit) },
                isLoading = state.isLoading,
                enabled = state.isFormValid,
            )
        }
    }
}
