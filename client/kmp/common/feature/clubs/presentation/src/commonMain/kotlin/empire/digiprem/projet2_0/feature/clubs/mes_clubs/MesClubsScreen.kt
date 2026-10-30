package empire.digiprem.projet2_0.feature.clubs.mes_clubs

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.states.PDZStateContent
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.presentation.util.ObservableEvent
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.model.MembershipRole
import empire.digiprem.projet2_0.feature.clubs.model.MembershipStatut
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MesClubsRoot(
    onNavigateToGestion: (clubId: String, role: MembershipRole) -> Unit,
    onNavigateToJoin: () -> Unit,
    onNavigateToCreate: () -> Unit,
    viewModel: MesClubsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.onAction(MesClubsAction.OnLoad) }

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            is MesClubsEvent.OnNavigateToGestion -> onNavigateToGestion(event.clubId, event.role)
            MesClubsEvent.OnNavigateToJoin -> onNavigateToJoin()
            MesClubsEvent.OnNavigateToCreate -> onNavigateToCreate()
        }
    }

    MesClubsScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun MesClubsScreen(
    state: MesClubsState,
    onAction: (MesClubsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        when {
            state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())

            state.memberships.isEmpty() -> Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                PDZStateContent(
                    icon = Icons.Default.Groups,
                    title = "Pas encore de 2-0",
                    description = "Rejoins le club de ton équipe avec son code, ou lance ton propre 2-0 avec tes potes.",
                    modifier = Modifier.weight(1f),
                )
                BottomActions(onAction)
            }

            else -> Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                Text(
                    text = "Mes clubs",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(PDZSpacing.space4),
                )
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.memberships) { membership ->
                        MembershipRow(membership = membership, onClick = { onAction(MesClubsAction.OnClubClick(membership.clubId, membership.role)) })
                    }
                }
                BottomActions(onAction)
            }
        }
    }
}

@Composable
private fun MembershipRow(membership: Membership, onClick: () -> Unit) {
    val sousTitre = when (membership.statut) {
        MembershipStatut.EN_ATTENTE -> "Demande en attente"
        else -> if (membership.role == MembershipRole.ADMINISTRATEUR) "Administrateur" else "Membre"
    }
    AppListRow(
        title = membership.clubNom,
        subtitle = sousTitre,
        onClick = onClick,
        leading = { AppAvatar(initials = membership.clubNom) },
    )
}

@Composable
private fun BottomActions(onAction: (MesClubsAction) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(PDZSpacing.space4),
        horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space3),
    ) {
        PDZButton(
            text = "Rejoindre un club",
            onClick = { onAction(MesClubsAction.OnJoinClick) },
            type = PDZButtonType.Secondary,
            modifier = Modifier.weight(1f),
        )
        PDZButton(
            text = "Créer un club",
            onClick = { onAction(MesClubsAction.OnCreateClick) },
            modifier = Modifier.weight(1f),
        )
    }
}
