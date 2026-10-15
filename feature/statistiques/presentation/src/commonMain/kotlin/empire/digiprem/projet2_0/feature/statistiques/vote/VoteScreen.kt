package empire.digiprem.projet2_0.feature.statistiques.vote

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.feedback.PDZToast
import empire.digiprem.projet2_0.core.design_system.components.list.AppListRow
import empire.digiprem.projet2_0.core.design_system.components.season.SeasonBoard
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.feature.statistiques.model.Candidat
import empire.digiprem.projet2_0.feature.statistiques.model.VoteHommeDuMatch
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VoteRoot(
    matchId: String,
    deuxZeroId: String,
    viewModel: VoteViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(matchId, deuxZeroId) { viewModel.onAction(VoteAction.OnInit(matchId, deuxZeroId)) }

    VoteScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun VoteScreen(
    state: VoteState,
    onAction: (VoteAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold { padding ->
            when {
                state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())
                state.vote == null -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())
                else -> VoteContent(state = state, vote = state.vote, onAction = onAction, modifier = Modifier.padding(padding).fillMaxSize())
            }
        }

        state.message?.let { message ->
            LaunchedEffect(message) {
                delay(3_000)
                onAction(VoteAction.OnMessageShown)
            }
            PDZToast(message = message.asString(), modifier = Modifier.align(Alignment.BottomCenter).padding(PDZSpacing.space4))
        }
    }
}

@Composable
private fun VoteContent(state: VoteState, vote: VoteHommeDuMatch, onAction: (VoteAction) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(PDZSpacing.space4)) {
        item {
            Text(text = "Homme du match", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(PDZSpacing.space4))
            SeasonBoard(libelleA = state.campA?.libelle.orEmpty(), libelleB = state.campB?.libelle.orEmpty(), scoreA = vote.match.scoreCampA ?: 0, scoreB = vote.match.scoreCampB ?: 0)
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(
                text = "Vote ouvert encore ${vote.heuresRestantes} h · ${vote.nombreVotes} votes sur ${vote.nombreEligibles}",
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.height(PDZSpacing.space2))
            Text(
                text = "Choisis un joueur des deux équipes. Tu ne peux pas voter pour toi.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(PDZSpacing.space4))
        }

        items(vote.eligibles) { candidat ->
            CandidatRow(
                candidat = candidat,
                statLigne = state.statLigne(candidat),
                estMoi = candidat.utilisateurId == state.currentUserId,
                selected = candidat.utilisateurId == state.candidatSelectionneId,
                onClick = { onAction(VoteAction.OnCandidatClick(candidat.utilisateurId)) },
            )
        }

        item {
            Spacer(Modifier.height(PDZSpacing.space4))
            val candidatChoisi = vote.eligibles.firstOrNull { it.utilisateurId == state.candidatSelectionneId }
            PDZButton(
                text = candidatChoisi?.let { "Voter pour ${it.pseudo}" } ?: "Choisis un joueur",
                enabled = candidatChoisi != null,
                isLoading = state.isActionLoading,
                onClick = { onAction(VoteAction.OnVoterClick) },
            )
        }
    }
}

@Composable
private fun CandidatRow(candidat: Candidat, statLigne: String, estMoi: Boolean, selected: Boolean, onClick: () -> Unit) {
    AppListRow(
        title = candidat.pseudo + if (estMoi) " (toi)" else "",
        subtitle = statLigne,
        leading = { AppAvatar(initials = candidat.pseudo) },
        onClick = if (estMoi) null else onClick,
        trailing = { if (!estMoi) RadioButton(selected = selected, onClick = onClick) },
    )
}
