package empire.digiprem.projet2_0.feature.deuxzero.create_deuxzero

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZeroVisibilite
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateDeuxZeroRoot(
    clubId: String,
    onInterneCreated: (deuxZeroId: String, clubId: String, campA: Camp, campB: Camp) -> Unit,
    onManageEffectif: (camp: Camp, clubId: String) -> Unit,
    onFinished: () -> Unit,
    viewModel: CreateDeuxZeroViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(clubId) { viewModel.onAction(CreateDeuxZeroAction.OnInit(clubId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            is CreateDeuxZeroEvent.OnInterneCreated -> onInterneCreated(event.deuxZeroId, event.clubId, event.campA, event.campB)
            is CreateDeuxZeroEvent.OnManageEffectif -> onManageEffectif(event.camp, event.clubId)
            CreateDeuxZeroEvent.OnFinished -> onFinished()
        }
    }

    CreateDeuxZeroScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun CreateDeuxZeroScreen(
    state: CreateDeuxZeroState,
    onAction: (CreateDeuxZeroAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = PDZSpacing.space4)) {
            when {
                state.codeCampBToShare != null -> ResultExterne(state = state, onAction = onAction)
                else -> Wizard(state = state, onAction = onAction)
            }
        }
    }
}

@Composable
private fun ColumnScope.Wizard(state: CreateDeuxZeroState, onAction: (CreateDeuxZeroAction) -> Unit) {
    Spacer(Modifier.height(PDZSpacing.space6))
    Text(text = "Nouveau 2-0", style = MaterialTheme.typography.headlineSmall)
    Text(text = "Étape ${state.step} sur 5", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.height(PDZSpacing.space6))

    Column(modifier = Modifier.weight(1f)) {
        when (state.step) {
            1 -> StepInfos(state, onAction)
            2 -> StepRegles(state, onAction)
            3 -> StepCalendrier(state, onAction)
            4 -> StepValidation(state, onAction)
            else -> StepCamps(state, onAction)
        }
    }

    state.errorMessage?.let {
        Text(text = it.asString(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(PDZSpacing.space2))
    }

    Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space3), modifier = Modifier.padding(bottom = PDZSpacing.space4)) {
        if (state.step > 1) {
            PDZButton(
                text = "Retour",
                type = PDZButtonType.Secondary,
                onClick = { onAction(CreateDeuxZeroAction.OnPreviousStep) },
                modifier = Modifier.weight(1f),
            )
        }
        PDZButton(
            text = if (state.step == 5) "Créer le 2-0" else "Continuer",
            onClick = { onAction(if (state.step == 5) CreateDeuxZeroAction.OnSubmit else CreateDeuxZeroAction.OnNextStep) },
            enabled = state.canContinue,
            isLoading = state.isLoading,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StepInfos(state: CreateDeuxZeroState, onAction: (CreateDeuxZeroAction) -> Unit) {
    Text(text = "Informations générales", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(PDZSpacing.space4))
    PDZTextField(
        value = state.nom,
        onValueChange = { onAction(CreateDeuxZeroAction.OnNomChange(it)) },
        label = "Nom du 2-0",
        placeholder = "Ex. Le 2-0 du quartier",
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(PDZSpacing.space4))
    PDZTextField(
        value = state.lieu,
        onValueChange = { onAction(CreateDeuxZeroAction.OnLieuChange(it)) },
        label = "Lieu habituel (facultatif)",
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(PDZSpacing.space4))
    Text(text = "Visibilité", style = MaterialTheme.typography.labelLarge)
    Spacer(Modifier.height(PDZSpacing.space2))
    Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
        DeuxZeroVisibilite.entries.forEach { visibilite ->
            FilterChip(
                selected = state.visibilite == visibilite,
                onClick = { onAction(CreateDeuxZeroAction.OnVisibiliteChange(visibilite)) },
                label = { Text(if (visibilite == DeuxZeroVisibilite.PUBLIQUE) "Publique" else "Privée") },
            )
        }
    }
}

@Composable
private fun StepRegles(state: CreateDeuxZeroState, onAction: (CreateDeuxZeroAction) -> Unit) {
    Text(text = "Règles du jeu", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(PDZSpacing.space4))

    Text(text = "Durée d'une mi-temps", style = MaterialTheme.typography.labelLarge)
    Spacer(Modifier.height(PDZSpacing.space2))
    Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
        listOf(45, 60).forEach { minutes ->
            FilterChip(
                selected = state.dureeMiTempsMinutes == minutes,
                onClick = { onAction(CreateDeuxZeroAction.OnDureeMiTempsChange(minutes)) },
                label = { Text("$minutes min") },
            )
        }
    }
    Spacer(Modifier.height(PDZSpacing.space4))

    Stepper(label = "Nombre de mi-temps", value = state.nombreMiTemps, range = 1..4) {
        onAction(CreateDeuxZeroAction.OnNombreMiTempsChange(it))
    }
    Spacer(Modifier.height(PDZSpacing.space4))
    Stepper(label = "Joueurs par camp au maximum", value = state.nombreMaxJoueursParCamp, range = 1..30) {
        onAction(CreateDeuxZeroAction.OnNombreMaxJoueursChange(it))
    }
    Spacer(Modifier.height(PDZSpacing.space4))
    Stepper(label = "Durée de la pause (min)", value = state.dureePauseMinutes, range = 0..30) {
        onAction(CreateDeuxZeroAction.OnDureePauseChange(it))
    }
    Spacer(Modifier.height(PDZSpacing.space4))

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "2-0 interne", style = MaterialTheme.typography.labelLarge)
            Text(
                text = "Les deux camps viennent du même club",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(checked = state.estInterne, onCheckedChange = { onAction(CreateDeuxZeroAction.OnEstInterneChange(it)) })
    }
}

@Composable
private fun StepCalendrier(state: CreateDeuxZeroState, onAction: (CreateDeuxZeroAction) -> Unit) {
    Text(text = "Calendrier", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(PDZSpacing.space4))
    Text(text = "Jours de match habituels", style = MaterialTheme.typography.labelLarge)
    Spacer(Modifier.height(PDZSpacing.space2))
    Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
        JOURS_SEMAINE.take(4).forEach { jour ->
            FilterChip(
                selected = jour in state.joursHabituels,
                onClick = { onAction(CreateDeuxZeroAction.OnToggleJour(jour)) },
                label = { Text(jour.take(3)) },
            )
        }
    }
    Spacer(Modifier.height(PDZSpacing.space2))
    Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
        JOURS_SEMAINE.drop(4).forEach { jour ->
            FilterChip(
                selected = jour in state.joursHabituels,
                onClick = { onAction(CreateDeuxZeroAction.OnToggleJour(jour)) },
                label = { Text(jour.take(3)) },
            )
        }
    }
    Spacer(Modifier.height(PDZSpacing.space4))
    PDZTextField(
        value = state.heureHabituelle,
        onValueChange = { onAction(CreateDeuxZeroAction.OnHeureHabituelleChange(it)) },
        label = "Heure de coup d'envoi (facultatif)",
        placeholder = "Ex. 15:00",
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun StepValidation(state: CreateDeuxZeroState, onAction: (CreateDeuxZeroAction) -> Unit) {
    Text(text = "Validation des résultats", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(PDZSpacing.space4))
    Text(text = "Délai de validation automatique", style = MaterialTheme.typography.labelLarge)
    Spacer(Modifier.height(PDZSpacing.space2))
    Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
        listOf(24, 48).forEach { heures ->
            FilterChip(
                selected = state.delaiValidationAutomatiqueHeures == heures,
                onClick = { onAction(CreateDeuxZeroAction.OnDelaiValidationChange(heures)) },
                label = { Text("${heures}h") },
            )
        }
    }
    Spacer(Modifier.height(PDZSpacing.space4))
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Transferts illimités par saison", style = MaterialTheme.typography.labelLarge)
        }
        Switch(
            checked = state.transfertsAutorisesParSaison == null,
            onCheckedChange = { illimite -> onAction(CreateDeuxZeroAction.OnTransfertsAutorisesChange(if (illimite) null else 3)) },
        )
    }
    if (state.transfertsAutorisesParSaison != null) {
        Spacer(Modifier.height(PDZSpacing.space4))
        Stepper(label = "Transferts autorisés par saison", value = state.transfertsAutorisesParSaison, range = 1..20) {
            onAction(CreateDeuxZeroAction.OnTransfertsAutorisesChange(it))
        }
    }
}

@Composable
private fun StepCamps(state: CreateDeuxZeroState, onAction: (CreateDeuxZeroAction) -> Unit) {
    Text(text = "Camps et saison", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(PDZSpacing.space4))
    PDZTextField(
        value = state.libelleCampA,
        onValueChange = { onAction(CreateDeuxZeroAction.OnLibelleCampAChange(it)) },
        label = "Nom du camp A",
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(PDZSpacing.space4))
    PDZTextField(
        value = state.libelleCampB,
        onValueChange = { onAction(CreateDeuxZeroAction.OnLibelleCampBChange(it)) },
        label = "Nom du camp B",
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(PDZSpacing.space4))
    PDZTextField(
        value = state.libelleSaison,
        onValueChange = { onAction(CreateDeuxZeroAction.OnLibelleSaisonChange(it)) },
        label = "Nom de la première saison",
        placeholder = "Ex. Saison 2026",
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(PDZSpacing.space4))
    PDZTextField(
        value = state.dateDebutSaison,
        onValueChange = { onAction(CreateDeuxZeroAction.OnDateDebutSaisonChange(it)) },
        label = "Date de début de saison",
        placeholder = "AAAA-MM-JJ",
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(PDZSpacing.space2))
    Text(
        text = "Un code d'engagement sera généré pour chaque camp — à transmettre aux administrateurs des clubs concernés.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun ResultExterne(state: CreateDeuxZeroState, onAction: (CreateDeuxZeroAction) -> Unit) {
    Spacer(Modifier.height(PDZSpacing.space8))
    Text(text = "2-0 créé", style = MaterialTheme.typography.headlineSmall)
    Spacer(Modifier.height(PDZSpacing.space2))
    Text(
        text = "Ton club occupe déjà le camp \"${state.libelleCampA}\". Transmets ce code à l'administrateur de l'autre club pour qu'il engage \"${state.libelleCampB}\".",
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(Modifier.height(PDZSpacing.space4))
    Text(text = state.codeCampBToShare.orEmpty(), style = MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(PDZSpacing.space8))
    PDZButton(text = "Composer l'effectif de mon camp", onClick = { onAction(CreateDeuxZeroAction.OnManageCampAEffectif) })
    Spacer(Modifier.height(PDZSpacing.space3))
    PDZButton(text = "Terminer", type = PDZButtonType.Text, onClick = { onAction(CreateDeuxZeroAction.OnFinish) })
}

@Composable
private fun Stepper(label: String, value: Int, range: IntRange, onValueChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
        IconButton(onClick = { if (value - 1 >= range.first) onValueChange(value - 1) }) {
            Icon(imageVector = Icons.Default.Remove, contentDescription = "Diminuer")
        }
        Text(text = "$value", style = MaterialTheme.typography.titleMedium, modifier = Modifier.width(32.dp))
        IconButton(onClick = { if (value + 1 <= range.last) onValueChange(value + 1) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Augmenter")
        }
    }
}
