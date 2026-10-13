package empire.digiprem.projet2_0.feature.auth.signup_contact

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignupContactRoot(
    onOtpSent: (contact: String) -> Unit,
    viewModel: SignupContactViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            is SignupContactEvent.OnOtpSent -> onOtpSent(event.contact)
        }
    }

    SignupContactScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun SignupContactScreen(
    state: SignupContactState,
    onAction: (SignupContactAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = PDZSpacing.space4),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(Modifier.height(PDZSpacing.space8))
            Text(text = "Ton numéro de téléphone", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(
                text = "On t'envoie un code par SMS pour vérifier que c'est bien toi.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(PDZSpacing.space6))

            PDZTextField(
                value = state.contact,
                onValueChange = { onAction(SignupContactAction.OnContactChange(it)) },
                label = if (state.isEmailMode) "E-mail" else "Numéro",
                placeholder = if (state.isEmailMode) "toi@exemple.com" else "+33 6 12 34 56 78",
                keyboardType = if (state.isEmailMode) KeyboardType.Email else KeyboardType.Phone,
                imeAction = ImeAction.Done,
                supportingText = state.errorMessage?.asString(),
                isError = state.errorMessage != null,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(PDZSpacing.space3))
            PDZButton(
                text = if (state.isEmailMode) "Utiliser un numéro de téléphone à la place"
                       else "Utiliser une adresse e-mail à la place",
                onClick = { onAction(SignupContactAction.OnToggleEmailMode) },
                type = PDZButtonType.Text,
                fullWidth = false,
            )

            Spacer(Modifier.height(PDZSpacing.space6))
            PDZButton(
                text = "Recevoir le code",
                onClick = { onAction(SignupContactAction.OnSubmit) },
                isLoading = state.isLoading,
                enabled = state.isFormValid,
            )

            Spacer(Modifier.height(PDZSpacing.space4))
            Text(
                text = "En continuant, tu acceptes les conditions d'utilisation et la politique de confidentialité.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
