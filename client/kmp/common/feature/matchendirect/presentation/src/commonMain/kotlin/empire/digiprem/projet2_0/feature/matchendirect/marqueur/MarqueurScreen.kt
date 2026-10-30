package empire.digiprem.projet2_0.feature.matchendirect.marqueur

import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.buttons.GoalButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.feedback.PDZToast
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.timeline.Timeline
import empire.digiprem.projet2_0.core.design_system.components.timeline.TimelineEntry
import empire.digiprem.projet2_0.core.design_system.components.timeline.TimelineSide
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenement
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenementSaisissable
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarqueurRoot(
    matchId: String,
    deuxZeroId: String,
    onMatchTermine: () -> Unit,
    viewModel: MarqueurViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(matchId, deuxZeroId) { viewModel.onAction(MarqueurAction.OnInit(matchId, deuxZeroId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            MarqueurEvent.OnMatchTermine -> onMatchTermine()
        }
    }

    MarqueurScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarqueurScreen(
    state: MarqueurState,
    onAction: (MarqueurAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().background(PDZColor.Gazon900)) {
        Scaffold(containerColor = PDZColor.Gazon900) { padding ->
            when {
                state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())

                state.match?.statut == "PROGRAMME" -> CoupDEnvoiContent(
                    isLoading = state.isActionLoading,
                    onClick = { onAction(MarqueurAction.OnDemarrerMatchClick) },
                    modifier = Modifier.padding(padding).fillMaxSize(),
                )

                else -> MarqueurContent(state = state, onAction = onAction, modifier = Modifier.padding(padding).fillMaxSize())
            }
        }

        state.message?.let { message ->
            LaunchedEffect(message) {
                delay(if (state.messageActionLabel != null) 10_000 else 3_000)
                onAction(MarqueurAction.OnMessageShown)
            }
            PDZToast(
                message = message.asString(),
                actionLabel = state.messageActionLabel,
                onActionClick = { onAction(MarqueurAction.OnAnnulerDernierClick) },
                modifier = Modifier.align(Alignment.BottomCenter).padding(PDZSpacing.space4),
            )
        }
    }

    if (state.showConfirmerFin) {
        AlertDialog(
            onDismissRequest = { onAction(MarqueurAction.OnDismissConfirmerFin) },
            title = { Text("Terminer le match ?") },
            text = { Text("Le score sera figé et le match passera en attente de validation.") },
            confirmButton = { TextButton(onClick = { onAction(MarqueurAction.OnConfirmerFinDuMatch) }) { Text("Terminer") } },
            dismissButton = { TextButton(onClick = { onAction(MarqueurAction.OnDismissConfirmerFin) }) { Text("Annuler") } },
        )
    }

    when (val sheet = state.sheet) {
        is MarqueurSheet.But -> ModalBottomSheet(onDismissRequest = { onAction(MarqueurAction.OnDismissSheet) }) {
            ButSheetContent(camp = sheet.camp, state = state, onAction = onAction)
        }
        is MarqueurSheet.JoueurUnique -> ModalBottomSheet(onDismissRequest = { onAction(MarqueurAction.OnDismissSheet) }) {
            JoueurUniqueSheetContent(sheet = sheet, state = state, onAction = onAction)
        }
        is MarqueurSheet.Remplacement -> ModalBottomSheet(onDismissRequest = { onAction(MarqueurAction.OnDismissSheet) }) {
            RemplacementSheetContent(camp = sheet.camp, state = state, onAction = onAction)
        }
        MarqueurSheet.Autre -> ModalBottomSheet(onDismissRequest = { onAction(MarqueurAction.OnDismissSheet) }) {
            AutreSheetContent(state = state, onAction = onAction)
        }
        null -> Unit
    }
}

@Composable
private fun CoupDEnvoiContent(isLoading: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(PDZSpacing.space4), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Prêt pour le coup d'envoi ?", style = MaterialTheme.typography.headlineSmall, color = PDZColor.Blanc)
        Spacer(Modifier.height(PDZSpacing.space6))
        PDZButton(text = "Lancer le coup d'envoi", isLoading = isLoading, onClick = onClick)
    }
}

@Composable
private fun MarqueurContent(state: MarqueurState, onAction: (MarqueurAction) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(PDZSpacing.space4)) {
        Text(
            text = "En direct · ${state.periodeActuelle}${if (state.periodeActuelle == 1) "re" else "e"} mi-temps, ${state.dureeMiTempsMinutes} min",
            style = MaterialTheme.typography.labelLarge,
            color = PDZColor.Ligne,
        )
        Spacer(Modifier.height(PDZSpacing.space2))
        Text(
            text = state.chronoLibelle,
            style = MaterialTheme.typography.displayMedium.copy(fontSize = 72.sp, fontWeight = FontWeight.Bold),
            color = PDZColor.Blanc,
        )
        Spacer(Modifier.height(PDZSpacing.space4))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = state.campA?.libelle.orEmpty(), style = MaterialTheme.typography.titleMedium, color = PDZColor.Blanc)
            Text(
                text = "${state.scoreCampA} – ${state.scoreCampB}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PDZColor.Blanc,
            )
            Text(text = state.campB?.libelle.orEmpty(), style = MaterialTheme.typography.titleMedium, color = PDZColor.Blanc)
        }
        Spacer(Modifier.height(PDZSpacing.space4))

        Timeline(
            entries = state.dernierEvenementsAffiches.map { it.toTimelineEntry(state.campA?.id) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(PDZSpacing.space6))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
            state.campA?.let { campA ->
                GoalButton(label = campA.libelle, campColor = PDZColor.CampA, onClick = { onAction(MarqueurAction.OnGoalButtonClick(campA)) }, modifier = Modifier.weight(1f))
            }
            state.campB?.let { campB ->
                GoalButton(label = campB.libelle, campColor = PDZColor.CampB, onClick = { onAction(MarqueurAction.OnGoalButtonClick(campB)) }, modifier = Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(PDZSpacing.space4))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
            PDZButton(text = "Annuler", type = PDZButtonType.Secondary, onClick = { onAction(MarqueurAction.OnAnnulerDernierClick) }, modifier = Modifier.weight(1f))
            PDZButton(text = "Autre", type = PDZButtonType.Secondary, onClick = { onAction(MarqueurAction.OnAutreClick) }, modifier = Modifier.weight(1f))
            PDZButton(text = "Fin du match", type = PDZButtonType.Secondary, onClick = { onAction(MarqueurAction.OnFinDuMatchClick) }, modifier = Modifier.weight(1f))
        }
    }
}

private fun Evenement.toTimelineEntry(campAId: String?): TimelineEntry {
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
    )
}

@Composable
private fun ButSheetContent(camp: Camp, state: MarqueurState, onAction: (MarqueurAction) -> Unit) {
    var contreSonCamp by remember { mutableStateOf(false) }
    var buteurId by remember { mutableStateOf<String?>(null) }
    var passeurId by remember { mutableStateOf<String?>(null) }

    val campButeur = if (contreSonCamp) state.campAdverse(camp) else camp
    val joueursButeur = state.effectifPresentDuCamp(campButeur)
    val joueursPasseur = state.effectifPresentDuCamp(camp).filter { it.utilisateurId != buteurId }

    Column(modifier = Modifier.padding(PDZSpacing.space4)) {
        Text(text = "But des ${camp.libelle}, ${state.minuteActuelle}'", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(PDZSpacing.space3))
        Text(text = "1. Buteur", style = MaterialTheme.typography.labelLarge)
        LazyColumn(modifier = Modifier.height(240.dp)) {
            items(joueursButeur) { joueur -> JoueurPickRow(joueur = joueur, selected = joueur.utilisateurId == buteurId, onClick = { buteurId = joueur.utilisateurId; passeurId = null }) }
            item {
                AppListRow(
                    title = "Contre son camp",
                    leading = { AppAvatar(initials = "?") },
                    onClick = { contreSonCamp = !contreSonCamp; buteurId = null; passeurId = null },
                )
            }
        }

        if (!contreSonCamp) {
            Spacer(Modifier.height(PDZSpacing.space3))
            Text(text = "2. Passeur", style = MaterialTheme.typography.labelLarge)
            LazyColumn(modifier = Modifier.height(200.dp)) {
                item { AppListRow(title = "Sans passe", onClick = { passeurId = null }) }
                items(joueursPasseur) { joueur -> JoueurPickRow(joueur = joueur, selected = joueur.utilisateurId == passeurId, onClick = { passeurId = joueur.utilisateurId }) }
            }
        }

        Spacer(Modifier.height(PDZSpacing.space4))
        PDZButton(
            text = "Enregistrer le but",
            enabled = buteurId != null,
            isLoading = state.isActionLoading,
            onClick = { onAction(MarqueurAction.OnEnregistrerBut(buteurId, passeurId, contreSonCamp)) },
        )
    }
}

@Composable
private fun JoueurUniqueSheetContent(sheet: MarqueurSheet.JoueurUnique, state: MarqueurState, onAction: (MarqueurAction) -> Unit) {
    var joueurId by remember { mutableStateOf<String?>(null) }
    val joueurs = state.effectifPresentDuCamp(sheet.camp)

    Column(modifier = Modifier.padding(PDZSpacing.space4)) {
        Text(text = "${sheet.titre} — ${sheet.camp.libelle}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(PDZSpacing.space3))
        LazyColumn(modifier = Modifier.height(280.dp)) {
            items(joueurs) { joueur -> JoueurPickRow(joueur = joueur, selected = joueur.utilisateurId == joueurId, onClick = { joueurId = joueur.utilisateurId }) }
        }
        Spacer(Modifier.height(PDZSpacing.space4))
        PDZButton(
            text = "Enregistrer",
            enabled = joueurId != null,
            isLoading = state.isActionLoading,
            onClick = { joueurId?.let { onAction(MarqueurAction.OnEnregistrerJoueurUnique(it)) } },
        )
    }
}

@Composable
private fun RemplacementSheetContent(camp: Camp, state: MarqueurState, onAction: (MarqueurAction) -> Unit) {
    var sortantId by remember { mutableStateOf<String?>(null) }
    var entrantId by remember { mutableStateOf<String?>(null) }
    val joueursDuCamp = state.effectifPresentDuCamp(camp)

    Column(modifier = Modifier.padding(PDZSpacing.space4)) {
        Text(text = "Remplacement — ${camp.libelle}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(PDZSpacing.space3))
        Text(text = "Sortant", style = MaterialTheme.typography.labelLarge)
        LazyColumn(modifier = Modifier.height(200.dp)) {
            items(joueursDuCamp) { joueur -> JoueurPickRow(joueur = joueur, selected = joueur.utilisateurId == sortantId, onClick = { sortantId = joueur.utilisateurId }) }
        }
        Spacer(Modifier.height(PDZSpacing.space3))
        Text(text = "Entrant", style = MaterialTheme.typography.labelLarge)
        LazyColumn(modifier = Modifier.height(200.dp)) {
            items(joueursDuCamp.filter { it.utilisateurId != sortantId }) { joueur -> JoueurPickRow(joueur = joueur, selected = joueur.utilisateurId == entrantId, onClick = { entrantId = joueur.utilisateurId }) }
        }
        Spacer(Modifier.height(PDZSpacing.space4))
        PDZButton(
            text = "Enregistrer",
            enabled = sortantId != null && entrantId != null,
            isLoading = state.isActionLoading,
            onClick = { if (sortantId != null && entrantId != null) onAction(MarqueurAction.OnEnregistrerRemplacement(sortantId!!, entrantId!!)) },
        )
    }
}

@Composable
private fun AutreSheetContent(state: MarqueurState, onAction: (MarqueurAction) -> Unit) {
    val camps = listOfNotNull(state.campA, state.campB)
    Column(modifier = Modifier.padding(PDZSpacing.space4)) {
        Text(text = "Autre événement", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(PDZSpacing.space2))
        camps.forEach { camp ->
            AppListRow(title = "Pénalty marqué — ${camp.libelle}", onClick = { onAction(MarqueurAction.OnAutreTypeClick(camp, TypeEvenementSaisissable.PENALTY_MARQUE, "Pénalty marqué")) })
            AppListRow(title = "Pénalty manqué — ${camp.libelle}", onClick = { onAction(MarqueurAction.OnAutreTypeClick(camp, TypeEvenementSaisissable.PENALTY_MANQUE, "Pénalty manqué")) })
            AppListRow(title = "Carton — ${camp.libelle}", onClick = { onAction(MarqueurAction.OnAutreTypeClick(camp, TypeEvenementSaisissable.CARTON, "Carton")) })
            AppListRow(title = "Remplacement — ${camp.libelle}", onClick = { onAction(MarqueurAction.OnRemplacementClick(camp)) })
        }
        AppListRow(title = "Passer la mi-temps", onClick = { onAction(MarqueurAction.OnPasserMiTempsClick) })
    }
}

@Composable
private fun JoueurPickRow(joueur: Effectif, selected: Boolean, onClick: () -> Unit) {
    AppListRow(
        title = joueur.pseudo,
        subtitle = joueur.numeroMaillot?.let { "#$it" },
        leading = { AppAvatar(initials = joueur.pseudo, ringColor = if (selected) PDZColor.Ligne else null) },
        onClick = onClick,
    )
}
