package empire.digiprem.projet2_0.feature.statistiques.bilan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.season.SeasonBoard
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.feature.statistiques.model.BilanSaison
import empire.digiprem.projet2_0.feature.statistiques.model.RecompenseJoueur
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BilanRoot(
    saisonId: String,
    deuxZeroId: String,
    viewModel: BilanViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(saisonId) { viewModel.onAction(BilanAction.OnInit(saisonId, deuxZeroId)) }

    BilanScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun BilanScreen(
    state: BilanState,
    onAction: (BilanAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        when {
            state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())
            state.bilan == null -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())
            else -> BilanContent(state = state, bilan = state.bilan, modifier = Modifier.padding(padding).fillMaxSize())
        }
    }
}

@Composable
private fun BilanContent(state: BilanState, bilan: BilanSaison, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(PDZSpacing.space4)) {
        item {
            Text(text = "Bilan de la saison ${bilan.saisonLibelle}", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(PDZSpacing.space4))
            Text(text = "Champions", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(PDZSpacing.space2))
            SeasonBoard(
                libelleA = state.campA?.libelle.orEmpty(),
                libelleB = state.campB?.libelle.orEmpty(),
                scoreA = bilan.scoreCampA,
                scoreB = bilan.scoreCampB,
                champion = state.campChampionLibelle,
            )
            Spacer(Modifier.height(PDZSpacing.space6))
        }

        bilan.joueurDeLAnnee?.let { item { RecompenseRow(cle = "Joueur de l'année", recompense = it) } }
        bilan.meilleurButeur?.let { item { RecompenseRow(cle = "Meilleur buteur", recompense = it) } }
        bilan.meilleurPasseur?.let { item { RecompenseRow(cle = "Meilleur passeur", recompense = it) } }
        bilan.plusAssidu?.let { item { RecompenseRow(cle = "Le plus assidu", recompense = it) } }
        bilan.meilleurGardien?.let { item { RecompenseRow(cle = "Meilleur gardien", recompense = it) } }

        item {
            Spacer(Modifier.height(PDZSpacing.space6))
            Text(text = "${bilan.nombreMatchs}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = "Matchs", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun RecompenseRow(cle: String, recompense: RecompenseJoueur) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = cle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "${recompense.pseudo}, ${recompense.valeurAffichee}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}
