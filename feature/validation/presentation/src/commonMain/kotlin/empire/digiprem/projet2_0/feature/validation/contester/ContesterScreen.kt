package empire.digiprem.projet2_0.feature.validation.contester

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ContesterRoot(
    matchId: String,
    onEnvoye: () -> Unit,
    viewModel: ContesterViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(matchId) { viewModel.onAction(ContesterAction.OnInit(matchId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            ContesterEvent.OnEnvoye -> onEnvoye()
        }
    }

    ContesterScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun ContesterScreen(
    state: ContesterState,
    onAction: (ContesterAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(PDZSpacing.space4),
        ) {
            item {
                Text(text = "Contester", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(PDZSpacing.space3))
                Text(
                    text = "Indique ce qui ne va pas. Le capitaine adverse pourra accepter ta correction ; sinon l'organisateur tranchera.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(PDZSpacing.space3))
                Text(text = "Qu'est-ce qui est faux ?", style = MaterialTheme.typography.titleMedium)
            }

            item { TypeErreurRow(label = "Le score", selected = state.typeErreur == TypeErreur.SCORE, onClick = { onAction(ContesterAction.OnTypeErreurChange(TypeErreur.SCORE)) }) }
            item { TypeErreurRow(label = "Un buteur ou un passeur", selected = state.typeErreur == TypeErreur.BUTEUR_OU_PASSEUR, onClick = { onAction(ContesterAction.OnTypeErreurChange(TypeErreur.BUTEUR_OU_PASSEUR)) }) }
            item { TypeErreurRow(label = "Le match n'a pas eu lieu", selected = state.typeErreur == TypeErreur.MATCH_NON_JOUE, onClick = { onAction(ContesterAction.OnTypeErreurChange(TypeErreur.MATCH_NON_JOUE)) }) }

            if (state.typeErreur == TypeErreur.BUTEUR_OU_PASSEUR) {
                item {
                    Spacer(Modifier.height(PDZSpacing.space3))
                    Text(text = "Événement à corriger", style = MaterialTheme.typography.titleMedium)
                }
                items(state.evenementsCorrigeables) { evenement ->
                    AppListRow(
                        title = "${evenement.minute}', ${evenement.joueurPseudo.orEmpty()}",
                        onClick = { onAction(ContesterAction.OnEvenementChange(evenement.id)) },
                        trailing = { RadioButton(selected = evenement.id == state.evenementACorrigerId, onClick = { onAction(ContesterAction.OnEvenementChange(evenement.id)) }) },
                    )
                }
                item {
                    Spacer(Modifier.height(PDZSpacing.space3))
                    PDZTextField(
                        value = state.bonJoueur,
                        onValueChange = { onAction(ContesterAction.OnBonJoueurChange(it)) },
                        label = "Bon buteur / passeur",
                    )
                }
            }

            item {
                Spacer(Modifier.height(PDZSpacing.space3))
                PDZTextField(
                    value = state.commentaire,
                    onValueChange = { onAction(ContesterAction.OnCommentaireChange(it)) },
                    label = "Commentaire (facultatif)",
                )
                Spacer(Modifier.height(PDZSpacing.space3))
                state.errorMessage?.let {
                    Text(text = it.asString(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(PDZSpacing.space3))
                }
                PDZButton(
                    text = "Envoyer la correction",
                    enabled = state.peutEnvoyer,
                    isLoading = state.isLoading,
                    onClick = { onAction(ContesterAction.OnEnvoyerClick) },
                )
            }
        }
    }
}

@Composable
private fun TypeErreurRow(label: String, selected: Boolean, onClick: () -> Unit) {
    AppListRow(
        title = label,
        onClick = onClick,
        trailing = { RadioButton(selected = selected, onClick = onClick) },
    )
}
