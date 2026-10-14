package empire.digiprem.projet2_0.feature.matchendirect.suivre

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.timeline.Timeline
import empire.digiprem.projet2_0.core.design_system.components.timeline.TimelineEntry
import empire.digiprem.projet2_0.core.design_system.components.timeline.TimelineSide
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenement
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SuivreEnDirectRoot(
    matchId: String,
    deuxZeroId: String,
    viewModel: SuivreEnDirectViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(matchId, deuxZeroId) { viewModel.onAction(SuivreEnDirectAction.OnInit(matchId, deuxZeroId)) }

    SuivreEnDirectScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun SuivreEnDirectScreen(
    state: SuivreEnDirectState,
    onAction: (SuivreEnDirectAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold { padding ->
            when {
                state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())

                else -> Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                    Text(
                        text = "En direct · ${state.minuteActuelle}'",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = PDZSpacing.space4, vertical = PDZSpacing.space2),
                    )
                    ScoreBoard(state = state)
                    Spacer(Modifier.height(PDZSpacing.space2))

                    TabRow(selectedTabIndex = state.tab.ordinal) {
                        Tab(
                            selected = state.tab == SuivreEnDirectTab.RESUME,
                            onClick = { onAction(SuivreEnDirectAction.OnTabChange(SuivreEnDirectTab.RESUME)) },
                            text = { Text("Résumé") },
                        )
                        Tab(
                            selected = state.tab == SuivreEnDirectTab.COMPOSITIONS,
                            onClick = { onAction(SuivreEnDirectAction.OnTabChange(SuivreEnDirectTab.COMPOSITIONS)) },
                            text = { Text("Compositions") },
                        )
                    }

                    when (state.tab) {
                        SuivreEnDirectTab.RESUME -> ResumeTab(state = state, modifier = Modifier.fillMaxSize())
                        SuivreEnDirectTab.COMPOSITIONS -> CompositionsTab(state = state, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreBoard(state: SuivreEnDirectState) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = PDZSpacing.space4),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = state.campA?.libelle.orEmpty(), style = MaterialTheme.typography.titleMedium)
        Text(
            text = "${state.scoreCampA} – ${state.scoreCampB}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(text = state.campB?.libelle.orEmpty(), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun ResumeTab(state: SuivreEnDirectState, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(PDZSpacing.space4)) {
        Timeline(
            entries = state.chronologie.map { it.toTimelineEntry(state.campA?.id, state.idEvenementSurligne) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(PDZSpacing.space4))
        state.match?.let { match ->
            Text(
                text = "Coup d'envoi à ${match.heure}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun Evenement.toTimelineEntry(campAId: String?, idSurligne: String?): TimelineEntry {
    val titre = when (type) {
        TypeEvenement.BUT -> joueurPseudo.orEmpty()
        TypeEvenement.BUT_CONTRE_SON_CAMP -> "${joueurPseudo.orEmpty()} (csc)"
        TypeEvenement.PENALTY_MARQUE -> "${joueurPseudo.orEmpty()} (penalty)"
        TypeEvenement.PENALTY_MANQUE -> "${joueurPseudo.orEmpty()} (penalty manqué)"
        TypeEvenement.ARRET_DECISIF -> "${joueurPseudo.orEmpty()} (arrêt décisif)"
        TypeEvenement.CARTON -> "${joueurPseudo.orEmpty()} (carton)"
        TypeEvenement.REMPLACEMENT -> "${joueurPseudo.orEmpty()} ➜ ${joueurSecondairePseudo.orEmpty()}"
        TypeEvenement.DEBUT_PERIODE, TypeEvenement.FIN_PERIODE -> type.name
    }
    val sousTitre = joueurSecondairePseudo?.takeIf { type == TypeEvenement.BUT }?.let { "passe de $it" }
    return TimelineEntry(
        id = id,
        minute = minute,
        title = titre,
        subtitle = sousTitre,
        side = if (campId == campAId) TimelineSide.A else TimelineSide.B,
        highlighted = id == idSurligne,
    )
}

@Composable
private fun CompositionsTab(state: SuivreEnDirectState, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(PDZSpacing.space4)) {
        item { CampComposition(camp = state.campA, titulaires = state.titulairesDuCamp(state.campA), remplacants = state.remplacantsDuCamp(state.campA)) }
        item { Spacer(Modifier.height(PDZSpacing.space6)) }
        item { CampComposition(camp = state.campB, titulaires = state.titulairesDuCamp(state.campB), remplacants = state.remplacantsDuCamp(state.campB)) }
    }
}

@Composable
private fun CampComposition(camp: Camp?, titulaires: List<Effectif>, remplacants: List<Effectif>) {
    Column {
        Text(text = camp?.libelle.orEmpty(), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(PDZSpacing.space2))
        titulaires.forEach { joueur -> AppListRow(title = joueur.pseudo, subtitle = "Titulaire", leading = { AppAvatar(initials = joueur.pseudo) }) }
        remplacants.forEach { joueur -> AppListRow(title = joueur.pseudo, subtitle = "Remplaçant", leading = { AppAvatar(initials = joueur.pseudo) }) }
    }
}
