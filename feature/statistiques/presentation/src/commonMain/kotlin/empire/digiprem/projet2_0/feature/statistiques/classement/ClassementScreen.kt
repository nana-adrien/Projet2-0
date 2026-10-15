package empire.digiprem.projet2_0.feature.statistiques.classement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.feature.statistiques.model.LigneClassement
import empire.digiprem.projet2_0.feature.statistiques.model.TypeClassement
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClassementRoot(
    deuxZeroId: String,
    onJoueurClick: (utilisateurId: String) -> Unit = {},
    viewModel: ClassementViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(deuxZeroId) { viewModel.onAction(ClassementAction.OnInit(deuxZeroId)) }

    ClassementScreen(state = state, onAction = viewModel::onAction, onJoueurClick = onJoueurClick)
}

@Composable
fun ClassementScreen(
    state: ClassementState,
    onAction: (ClassementAction) -> Unit,
    modifier: Modifier = Modifier,
    onJoueurClick: (utilisateurId: String) -> Unit = {},
) {
    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text(text = "Classements", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(PDZSpacing.space4))

            TabRow(selectedTabIndex = TypeClassement.entries.indexOf(state.type)) {
                Tab(selected = state.type == TypeClassement.BUTEURS, onClick = { onAction(ClassementAction.OnTypeChange(TypeClassement.BUTEURS)) }, text = { Text("Buteurs") })
                Tab(selected = state.type == TypeClassement.PASSEURS, onClick = { onAction(ClassementAction.OnTypeChange(TypeClassement.PASSEURS)) }, text = { Text("Passeurs") })
                Tab(selected = state.type == TypeClassement.VOTES, onClick = { onAction(ClassementAction.OnTypeChange(TypeClassement.VOTES)) }, text = { Text("Votes") })
            }

            if (state.isLoading) {
                PDZLoadingSpinner(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(state.lignes) { index, ligne ->
                        LigneClassementRow(
                            rang = index + 1,
                            ligne = ligne,
                            estMoi = ligne.utilisateurId == state.currentUserId,
                            onClick = { onJoueurClick(ligne.utilisateurId) },
                        )
                    }
                }
                Text(
                    text = "À égalité, le joueur qui a joué le moins de matchs passe devant.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(PDZSpacing.space4),
                )
            }
        }
    }
}

@Composable
private fun LigneClassementRow(rang: Int, ligne: LigneClassement, estMoi: Boolean, onClick: () -> Unit) {
    val fond = if (estMoi) PDZColor.Gazon50 else Color.Transparent
    Box(modifier = Modifier.fillMaxWidth().background(fond)) {
        AppListRow(
            title = ligne.pseudo + if (estMoi) " (toi)" else "",
            subtitle = "${ligne.campLibelle}, ${ligne.nombreMatchs} matchs",
            onClick = onClick,
            leading = {
                Row {
                    Text(
                        text = "$rang",
                        color = if (rang == 1) PDZColor.Ligne else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    AppAvatar(initials = ligne.pseudo)
                }
            },
            trailing = { Text(text = "${ligne.valeur}", style = MaterialTheme.typography.titleMedium) },
        )
    }
}
