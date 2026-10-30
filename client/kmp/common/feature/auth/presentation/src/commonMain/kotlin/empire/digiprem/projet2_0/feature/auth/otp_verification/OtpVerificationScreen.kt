package empire.digiprem.projet2_0.feature.auth.otp_verification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZOtpTextField
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OtpVerificationRoot(
    contact: String,
    purpose: OtpPurpose,
    onVerifiedForSignup: (contact: String, verificationToken: String) -> Unit,
    onVerifiedForPasswordReset: (contact: String, verificationToken: String) -> Unit,
    onLoggedIn: () -> Unit,
    onEditContact: () -> Unit = {},
    viewModel: OtpVerificationViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(contact, purpose) {
        viewModel.onAction(OtpVerificationAction.OnInit(contact, purpose))
    }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            is OtpVerificationEvent.OnVerifiedForSignup -> onVerifiedForSignup(event.contact, event.verificationToken)
            is OtpVerificationEvent.OnVerifiedForPasswordReset ->
                onVerifiedForPasswordReset(event.contact, event.verificationToken)
            OtpVerificationEvent.OnLoggedIn -> onLoggedIn()
        }
    }

    OtpVerificationScreen(state = state, onAction = viewModel::onAction, onEditContact = onEditContact)
}

@Composable
fun OtpVerificationScreen(
    state: OtpVerificationState,
    onAction: (OtpVerificationAction) -> Unit,
    modifier: Modifier = Modifier,
    onEditContact: () -> Unit = {},
) {
    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = PDZSpacing.space4),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(PDZSpacing.space8))
            Text(text = "Entre le code reçu", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(PDZSpacing.space2))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Envoyé à ${state.contact}. ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "Modifier",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onEditContact),
                )
            }
            Spacer(Modifier.height(PDZSpacing.space6))

            PDZOtpTextField(
                value = state.code,
                onValueChange = { onAction(OtpVerificationAction.OnCodeChange(it)) },
                isError = state.errorMessage != null,
            )

            if (state.isLoading) {
                Spacer(Modifier.height(PDZSpacing.space4))
                CircularProgressIndicator(modifier = Modifier.height(24.dp))
            }

            state.errorMessage?.let {
                Spacer(Modifier.height(PDZSpacing.space3))
                Text(
                    text = it.asString(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(PDZSpacing.space6))
            if (state.secondsBeforeResend > 0) {
                Text(
                    text = "Renvoyer le code dans 0:${state.secondsBeforeResend.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                PDZButton(
                    text = "Renvoyer le code",
                    onClick = { onAction(OtpVerificationAction.OnResendClick) },
                    type = PDZButtonType.Text,
                    fullWidth = false,
                )
            }

            Spacer(Modifier.height(PDZSpacing.space2))
            Text(
                text = "Le code se remplit tout seul quand le SMS arrive.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
