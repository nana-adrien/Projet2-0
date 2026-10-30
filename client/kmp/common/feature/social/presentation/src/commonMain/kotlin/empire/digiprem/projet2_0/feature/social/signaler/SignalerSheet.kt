package empire.digiprem.projet2_0.feature.social.signaler

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.social.model.TypeContenuSignale
import org.koin.compose.viewmodel.koinViewModel

/** Composant réutilisable "Signaler" (CU27, F-SOC-05) — à invoquer depuis n'importe quel écran
 * affichant du contenu ou un comportement signalable. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignalerSheet(
    contenuType: TypeContenuSignale,
    contenuId: String,
    onDismissRequest: () -> Unit,
    onEnvoye: () -> Unit,
    viewModel: SignalerViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(contenuType, contenuId) { viewModel.onAction(SignalerAction.OnInit(contenuType, contenuId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            SignalerEvent.OnEnvoye -> onEnvoye()
        }
    }

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(modifier = Modifier.padding(PDZSpacing.space4)) {
            Text(text = "Signaler", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(
                text = "Explique ce qui ne va pas. Un administrateur de la plateforme examinera ton signalement.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(PDZSpacing.space3))
            PDZTextField(
                value = state.motif,
                onValueChange = { viewModel.onAction(SignalerAction.OnMotifChange(it)) },
                label = "Motif",
                modifier = Modifier.fillMaxWidth(),
            )
            state.errorMessage?.let {
                Spacer(Modifier.height(PDZSpacing.space2))
                Text(text = it.asString(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(PDZSpacing.space4))
            PDZButton(
                text = "Envoyer le signalement",
                enabled = state.peutEnvoyer,
                isLoading = state.isLoading,
                onClick = { viewModel.onAction(SignalerAction.OnEnvoyerClick) },
            )
        }
    }
}
