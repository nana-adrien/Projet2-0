package empire.digiprem.projet2_0.feature.deuxzero.parametres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.dialog.PDZConfirmDialog
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.components.topbar.PDZTopBar
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.deuxzero.create_deuxzero.JOURS_SEMAINE
import empire.digiprem.projet2_0.feature.deuxzero.create_deuxzero.JOUR_LABEL
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZeroVisibilite
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ParametresDeuxZeroRoot(
    deuxZeroId: String,
    onOrganisateurTransfere: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: ParametresDeuxZeroViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(deuxZeroId) { viewModel.onAction(ParametresDeuxZeroAction.OnInit(deuxZeroId)) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            ParametresDeuxZeroEvent.OnOrganisateurTransfere -> onOrganisateurTransfere()
        }
    }

    ParametresDeuxZeroScreen(state = state, onAction = viewModel::onAction, onNavigateBack = onNavigateBack)
}

/** CU13 — Modifier les paramètres d'un 2-0. Pas d'écran dédié dans les maquettes. */
@Composable
fun ParametresDeuxZeroScreen(
    state: ParametresDeuxZeroState,
    onAction: (ParametresDeuxZeroAction) -> Unit,
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { PDZTopBar(title = "Paramètres du 2-0", onNavigateBack = onNavigateBack) },
    ) { padding ->
        if (state.isLoading) {
            PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())
            return@Scaffold
        }

        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = PDZSpacing.space4).verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(PDZSpacing.space4))
            Text(text = "Calendrier", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(text = "Jours de match habituels", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(PDZSpacing.space2))
            Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
                JOURS_SEMAINE.take(4).forEach { jour ->
                    FilterChip(
                        selected = jour in state.joursHabituels,
                        onClick = { onAction(ParametresDeuxZeroAction.OnToggleJour(jour)) },
                        label = { Text(JOUR_LABEL[jour] ?: jour) },
                    )
                }
            }
            Spacer(Modifier.height(PDZSpacing.space2))
            Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
                JOURS_SEMAINE.drop(4).forEach { jour ->
                    FilterChip(
                        selected = jour in state.joursHabituels,
                        onClick = { onAction(ParametresDeuxZeroAction.OnToggleJour(jour)) },
                        label = { Text(JOUR_LABEL[jour] ?: jour) },
                    )
                }
            }
            Spacer(Modifier.height(PDZSpacing.space4))

            PDZTextField(
                value = state.heureHabituelle,
                onValueChange = { onAction(ParametresDeuxZeroAction.OnHeureHabituelleChange(it)) },
                label = "Heure habituelle",
                placeholder = "HH:mm",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            PDZTextField(
                value = state.dureeMiTempsMinutes,
                onValueChange = { onAction(ParametresDeuxZeroAction.OnDureeMiTempsChange(it)) },
                label = "Durée d'une mi-temps (minutes)",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            PDZTextField(
                value = state.delaiValidationAutomatiqueHeures,
                onValueChange = { onAction(ParametresDeuxZeroAction.OnDelaiValidationChange(it)) },
                label = "Délai de validation automatique (heures)",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            PDZTextField(
                value = state.transfertsAutorisesParSaison,
                onValueChange = { onAction(ParametresDeuxZeroAction.OnTransfertsAutorisesChange(it)) },
                label = "Transferts autorisés par saison",
                placeholder = "Illimité si vide",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            Text(text = "Visibilité", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(PDZSpacing.space2))
            Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
                DeuxZeroVisibilite.entries.forEach { visibilite ->
                    FilterChip(
                        selected = state.visibilite == visibilite,
                        onClick = { onAction(ParametresDeuxZeroAction.OnVisibiliteChange(visibilite)) },
                        label = { Text(if (visibilite == DeuxZeroVisibilite.PUBLIQUE) "Public" else "Privé") },
                    )
                }
            }

            state.errorMessage?.let {
                Spacer(Modifier.height(PDZSpacing.space3))
                Text(text = it.asString(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            state.message?.let {
                Spacer(Modifier.height(PDZSpacing.space3))
                Text(text = it.asString(), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(PDZSpacing.space6))
            PDZButton(
                text = "Enregistrer",
                onClick = { onAction(ParametresDeuxZeroAction.OnSaveClick) },
                enabled = state.isFormValid,
                isLoading = state.isSaving,
            )

            Spacer(Modifier.height(PDZSpacing.space8))
            Text(text = "Rôle d'organisateur", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(
                text = "Transférer le rôle d'organisateur à un autre membre du 2-0.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(PDZSpacing.space2))
            if (state.membres.isEmpty()) {
                Text(
                    text = "Aucun membre engagé pour l'instant.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                LazyColumn(modifier = Modifier.height((state.membres.size * 64).coerceAtMost(320).dp)) {
                    items(state.membres) { membre ->
                        AppListRow(
                            title = membre.pseudo,
                            subtitle = membre.numeroMaillot?.let { "N°$it" },
                            onClick = { onAction(ParametresDeuxZeroAction.OnTransferOrganisateurClick(membre)) },
                        )
                    }
                }
            }

            Spacer(Modifier.height(PDZSpacing.space8))
            Text(text = "Archiver", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(
                text = if (state.estArchive) "Ce 2-0 est archivé." else "Archive ce 2-0 s'il n'est plus actif.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(PDZSpacing.space2))
            PDZButton(
                text = if (state.estArchive) "Réactiver ce 2-0" else "Archiver ce 2-0",
                type = PDZButtonType.Secondary,
                onClick = { onAction(ParametresDeuxZeroAction.OnToggleArchiveClick) },
                isLoading = state.isSaving,
            )
            Spacer(Modifier.height(PDZSpacing.space6))
        }
    }

    if (state.showTransferOrganisateurDialog && state.targetOrganisateur != null) {
        PDZConfirmDialog(
            title = "Transférer le rôle d'organisateur ?",
            message = "${state.targetOrganisateur.pseudo} deviendra organisateur de ce 2-0. Tu perds ce rôle immédiatement.",
            onConfirm = { onAction(ParametresDeuxZeroAction.OnConfirmTransferOrganisateur) },
            onCancel = { onAction(ParametresDeuxZeroAction.OnDismissTransferOrganisateurDialog) },
            onDismiss = { onAction(ParametresDeuxZeroAction.OnDismissTransferOrganisateurDialog) },
            confirmText = "Transférer",
            cancelText = "Annuler",
        )
    }
}
