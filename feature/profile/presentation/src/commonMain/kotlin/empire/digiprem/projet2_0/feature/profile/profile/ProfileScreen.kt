package empire.digiprem.projet2_0.feature.profile.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.dialog.PDZConfirmDialog
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.components.topbar.PDZTopBar
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.domain.model.PiedFort
import empire.digiprem.projet2_0.core.domain.model.Poste
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoot(
    onAccountDeleted: () -> Unit = {},
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            ProfileEvent.OnProfileUpdated -> Unit
            ProfileEvent.OnAccountDeleted -> onAccountDeleted()
        }
    }

    ProfileScreen(state = state, onAction = viewModel::onAction)
}

/** CU4 — Modifier son profil (et supprimer son compte). Pas d'écran dédié dans les maquettes,
 * atteint depuis E19 (bouton "Modifier le profil"). */
@Composable
fun ProfileScreen(
    state   : ProfileState,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar   = { PDZTopBar(title = "Mon profil") },
    ) { padding ->
        if (state.isLoading) {
            PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())
            return@Scaffold
        }

        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = PDZSpacing.space4)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(PDZSpacing.space4))
            AppAvatar(initials = state.pseudo, size = 72.dp)
            Spacer(Modifier.height(PDZSpacing.space6))

            PDZTextField(
                value         = state.pseudo,
                onValueChange = { onAction(ProfileAction.OnPseudoChange(it)) },
                label         = "Pseudo",
                modifier      = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            Text(text = "Poste préféré", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(PDZSpacing.space2))
            Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2), modifier = Modifier.fillMaxWidth()) {
                Poste.entries.forEach { poste ->
                    SegmentedChip(
                        label    = poste.label,
                        selected = state.postePrefere == poste,
                        onClick  = { onAction(ProfileAction.OnPosteChange(poste)) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            Spacer(Modifier.height(PDZSpacing.space4))

            Text(text = "Pied fort", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(PDZSpacing.space2))
            Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2), modifier = Modifier.fillMaxWidth()) {
                PiedFort.entries.forEach { pied ->
                    SegmentedChip(
                        label    = pied.label,
                        selected = state.piedFort == pied,
                        onClick  = { onAction(ProfileAction.OnPiedFortChange(pied)) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            Spacer(Modifier.height(PDZSpacing.space4))

            PDZTextField(
                value         = state.prenom,
                onValueChange = { onAction(ProfileAction.OnPrenomChange(it)) },
                label         = "Prénom (facultatif)",
                modifier      = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            PDZTextField(
                value         = state.nom,
                onValueChange = { onAction(ProfileAction.OnNomChange(it)) },
                label         = "Nom (facultatif)",
                modifier      = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space4))

            PDZTextField(
                value          = state.dateNaissance,
                onValueChange  = { onAction(ProfileAction.OnDateNaissanceChange(it)) },
                label          = "Date de naissance (facultatif)",
                placeholder    = "AAAA-MM-JJ",
                imeAction      = ImeAction.Done,
                modifier       = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(PDZSpacing.space6))

            SwitchRow(
                label    = "Masquer mon nom réel",
                checked  = state.cacherNomReel,
                onCheckedChange = { onAction(ProfileAction.OnCacherNomReelChange(it)) },
            )
            SwitchRow(
                label    = "Masquer mon âge",
                checked  = state.cacherAge,
                onCheckedChange = { onAction(ProfileAction.OnCacherAgeChange(it)) },
            )

            state.errorMessage?.let {
                Spacer(Modifier.height(PDZSpacing.space3))
                Text(
                    text  = it.asString(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(PDZSpacing.space6))
            PDZButton(
                text      = "Enregistrer",
                onClick   = { onAction(ProfileAction.OnSaveClick) },
                enabled   = state.isFormValid,
                isLoading = state.isSaving,
            )

            Spacer(Modifier.height(PDZSpacing.space6))
            PDZButton(
                text      = "Supprimer mon compte",
                type      = PDZButtonType.Text,
                onClick   = { onAction(ProfileAction.OnDeleteAccountClick) },
                isLoading = state.isDeleting,
            )
            Spacer(Modifier.height(PDZSpacing.space6))
        }
    }

    if (state.showDeleteDialog) {
        PDZConfirmDialog(
            title     = "Supprimer ton compte ?",
            message   = "Ton pseudo et ton historique de matchs sont conservés (scores et classements inchangés), mais tu apparaîtras comme \"Ancien joueur\". Cette action est irréversible.",
            onConfirm = { onAction(ProfileAction.OnConfirmDeleteAccount) },
            onCancel  = { onAction(ProfileAction.OnDismissDeleteDialog) },
            onDismiss = { onAction(ProfileAction.OnDismissDeleteDialog) },
            confirmText = "Supprimer",
            cancelText  = "Annuler",
        )
    }
}

@Composable
private fun SegmentedChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) PDZColor.Gazon else MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(vertical = PDZSpacing.space2),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) PDZColor.Blanc else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = PDZSpacing.space1),
        )
    }
}

@Composable
private fun SwitchRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier             = Modifier.fillMaxWidth().padding(vertical = PDZSpacing.space1),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment    = Alignment.CenterVertically,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
