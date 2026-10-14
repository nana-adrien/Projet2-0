package empire.digiprem.projet2_0.feature.clubs.gestion_club

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.dialog.PDZConfirmDialog
import empire.digiprem.projet2_0.core.design_system.components.feedback.PDZToast
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.clubs.model.Club
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.model.MembershipRole
import empire.digiprem.projet2_0.feature.clubs.model.MembershipRole.ADMINISTRATEUR
import empire.digiprem.projet2_0.feature.clubs.model.MembershipStatut
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GestionClubRoot(
    clubId: String,
    initialRole: MembershipRole,
    onLeftClub: () -> Unit,
    viewModel: GestionClubViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(clubId, initialRole) {
        viewModel.onAction(GestionClubAction.OnInit(clubId, initialRole))
    }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            GestionClubEvent.OnLeftClub -> onLeftClub()
        }
    }

    GestionClubScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun GestionClubScreen(
    state: GestionClubState,
    onAction: (GestionClubAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var memberToRemove by remember { mutableStateOf<Membership?>(null) }
    var showLeaveConfirm by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold { padding ->
            when {
                state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())

                state.club == null -> Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                    Text(
                        text = state.errorMessage?.asString() ?: "Impossible de charger le club.",
                        modifier = Modifier.padding(PDZSpacing.space4),
                    )
                    PDZButton(
                        text = "Réessayer",
                        onClick = { onAction(GestionClubAction.OnRetry) },
                        modifier = Modifier.padding(horizontal = PDZSpacing.space4),
                    )
                }

                else -> LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = PaddingValues(PDZSpacing.space4),
                ) {
                    item { ClubHeader(club = state.club) }
                    item { Spacer(Modifier.height(PDZSpacing.space4)) }
                    item {
                        CodeCard(
                            club = state.club,
                            isAdmin = state.isCurrentUserAdmin,
                            onRegenerateClick = { onAction(GestionClubAction.OnRegenerateCodeClick) },
                        )
                    }

                    if (state.isCurrentUserAdmin) {
                        item { Spacer(Modifier.height(PDZSpacing.space4)) }
                        item {
                            ReglagesSection(
                                club = state.club,
                                onToggleAcceptation = { onAction(GestionClubAction.OnToggleAcceptationAutomatique(it)) },
                                onToggleDelegation = { onAction(GestionClubAction.OnToggleDelegationCapitaines(it)) },
                            )
                        }
                    }

                    if (state.isCurrentUserAdmin && state.demandesEnAttente.isNotEmpty()) {
                        item { Spacer(Modifier.height(PDZSpacing.space4)) }
                        item {
                            Text(text = "Demandes en attente", style = MaterialTheme.typography.titleMedium)
                        }
                        items(state.demandesEnAttente) { membership ->
                            DemandeRow(
                                membership = membership,
                                onAccept = { onAction(GestionClubAction.OnRespondToRequest(membership.utilisateurId, true)) },
                                onReject = { onAction(GestionClubAction.OnRespondToRequest(membership.utilisateurId, false)) },
                            )
                        }
                    }

                    item { Spacer(Modifier.height(PDZSpacing.space4)) }
                    item { Text(text = "Membres", style = MaterialTheme.typography.titleMedium) }
                    items(state.membresActifs) { membership ->
                        MembreRow(
                            membership = membership,
                            isAdmin = state.isCurrentUserAdmin,
                            isSelf = membership.utilisateurId == state.currentUserId,
                            onNameCoAdmin = { onAction(GestionClubAction.OnNameCoAdmin(membership.utilisateurId)) },
                            onTransferAdmin = { onAction(GestionClubAction.OnTransferAdmin(membership.utilisateurId)) },
                            onRemove = { memberToRemove = membership },
                        )
                    }

                    item { Spacer(Modifier.height(PDZSpacing.space6)) }
                    item {
                        PDZButton(
                            text = "Quitter le club",
                            type = PDZButtonType.Secondary,
                            onClick = { showLeaveConfirm = true },
                        )
                    }
                }
            }
        }

        state.message?.let { message ->
            LaunchedEffect(message) {
                delay(3_000)
                onAction(GestionClubAction.OnMessageShown)
            }
            PDZToast(
                message = message.asString(),
                modifier = Modifier.align(Alignment.BottomCenter).padding(PDZSpacing.space4),
            )
        }
    }

    memberToRemove?.let { membership ->
        PDZConfirmDialog(
            title = "Retirer ce membre ?",
            message = "${membership.pseudo} ne fera plus partie du club.",
            onConfirm = { onAction(GestionClubAction.OnRemoveMember(membership.utilisateurId)) },
            onCancel = {},
            onDismiss = { memberToRemove = null },
        )
    }

    if (showLeaveConfirm) {
        PDZConfirmDialog(
            title = "Quitter le club ?",
            message = "Tu devras connaître le code d'adhésion pour revenir.",
            onConfirm = { onAction(GestionClubAction.OnLeaveClubClick) },
            onCancel = {},
            onDismiss = { showLeaveConfirm = false },
        )
    }
}

@Composable
private fun ClubHeader(club: Club) {
    Column {
        Text(text = club.nom, style = MaterialTheme.typography.headlineSmall)
        club.quartier?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        club.description?.let {
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(text = it, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun CodeCard(club: Club, isAdmin: Boolean, onRegenerateClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(PDZSpacing.space4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Code d'adhésion", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = club.codeAdhesion, style = MaterialTheme.typography.headlineMedium)
            }
            if (isAdmin) {
                IconButton(onClick = onRegenerateClick) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Régénérer le code")
                }
            }
        }
    }
}

@Composable
private fun ReglagesSection(
    club: Club,
    onToggleAcceptation: (Boolean) -> Unit,
    onToggleDelegation: (Boolean) -> Unit,
) {
    Column {
        Text(text = "Réglages", style = MaterialTheme.typography.titleMedium)
        AppListRow(
            title = "Acceptation automatique",
            subtitle = "Les demandes d'adhésion sont acceptées sans validation.",
            trailing = { Switch(checked = club.acceptationAutomatique, onCheckedChange = onToggleAcceptation) },
        )
        AppListRow(
            title = "Délégation aux capitaines",
            subtitle = "Les capitaines peuvent agir à la place de l'administrateur.",
            trailing = { Switch(checked = club.delegationCapitaines, onCheckedChange = onToggleDelegation) },
        )
    }
}

@Composable
private fun DemandeRow(membership: Membership, onAccept: () -> Unit, onReject: () -> Unit) {
    AppListRow(
        title = membership.pseudo,
        subtitle = "Demande d'adhésion",
        leading = { AppAvatar(initials = membership.pseudo) },
        trailing = {
            IconButton(onClick = onReject) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Refuser", tint = MaterialTheme.colorScheme.error)
            }
            IconButton(onClick = onAccept) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Accepter", tint = MaterialTheme.colorScheme.primary)
            }
        },
    )
}

@Composable
private fun MembreRow(
    membership: Membership,
    isAdmin: Boolean,
    isSelf: Boolean,
    onNameCoAdmin: () -> Unit,
    onTransferAdmin: () -> Unit,
    onRemove: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val sousTitre = if (membership.role == ADMINISTRATEUR) "Administrateur" else "Membre"

    AppListRow(
        title = if (isSelf) "${membership.pseudo} (toi)" else membership.pseudo,
        subtitle = sousTitre,
        leading = { AppAvatar(initials = membership.pseudo) },
        trailing = {
            if (isAdmin && !isSelf) {
                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Actions")
                    }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Nommer co-administrateur") },
                            onClick = { menuExpanded = false; onNameCoAdmin() },
                        )
                        DropdownMenuItem(
                            text = { Text("Transférer le rôle d'administrateur") },
                            onClick = { menuExpanded = false; onTransferAdmin() },
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Retirer du club") },
                            onClick = { menuExpanded = false; onRemove() },
                        )
                    }
                }
            }
        },
    )
}
