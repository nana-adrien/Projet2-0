package empire.digiprem.projet2_0.feature.statistiques.profil

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.avatar.AppAvatar
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.segmented.PDZSegmentedControl
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.feature.statistiques.model.BadgeObtenu
import empire.digiprem.projet2_0.feature.statistiques.model.ContexteStats
import empire.digiprem.projet2_0.feature.statistiques.model.MoisValeur
import empire.digiprem.projet2_0.feature.social.model.TypeContenuSignale
import empire.digiprem.projet2_0.feature.social.signaler.SignalerSheet
import empire.digiprem.projet2_0.feature.statistiques.model.ProfilJoueur
import empire.digiprem.projet2_0.feature.statistiques.model.TypeBadge
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfilRoot(
    deuxZeroId: String,
    utilisateurId: String? = null,
    viewModel: ProfilViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(utilisateurId, deuxZeroId) { viewModel.onAction(ProfilAction.OnInit(utilisateurId, deuxZeroId)) }

    ProfilScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun ProfilScreen(
    state: ProfilState,
    onAction: (ProfilAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        when {
            state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())

            else -> {
                val profil = state.profil
                if (profil == null) {
                    PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())
                } else {
                    ProfilContent(
                        profil = profil,
                        contexte = state.contexte,
                        montrerSignaler = !state.estMonPropreProfil,
                        onAction = onAction,
                        modifier = Modifier.padding(padding).fillMaxSize(),
                    )
                }
            }
        }
    }

    if (state.showSignaler) {
        SignalerSheet(
            contenuType = TypeContenuSignale.PROFIL_UTILISATEUR,
            contenuId = state.utilisateurId,
            onDismissRequest = { onAction(ProfilAction.OnDismissSignaler) },
            onEnvoye = { onAction(ProfilAction.OnDismissSignaler) },
        )
    }
}

@Composable
private fun ProfilContent(profil: ProfilJoueur, contexte: ContexteStats, montrerSignaler: Boolean, onAction: (ProfilAction) -> Unit, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(PDZSpacing.space4),
        horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2),
        verticalArrangement = Arrangement.spacedBy(PDZSpacing.space2),
    ) {
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppAvatar(initials = profil.pseudo, size = 72.dp)
                    Spacer(Modifier.width(PDZSpacing.space3))
                    Column {
                        Text(text = profil.pseudo, style = MaterialTheme.typography.headlineSmall)
                        profil.poste?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                        Text(text = profil.clubs.joinToString(" · "), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(PDZSpacing.space4))
                PDZSegmentedControl(
                    options = listOf("2-0", "Amicaux", "Total"),
                    selectedIndex = ContexteStats.entries.indexOf(contexte),
                    onOptionSelected = { onAction(ProfilAction.OnContexteChange(ContexteStats.entries[it])) },
                )
                if (montrerSignaler) {
                    Spacer(Modifier.height(PDZSpacing.space2))
                    PDZButton(
                        text = "Signaler ce profil",
                        type = PDZButtonType.Text,
                        fullWidth = false,
                        onClick = { onAction(ProfilAction.OnSignalerClick) },
                    )
                }
                Spacer(Modifier.height(PDZSpacing.space4))
            }
        }

        item { StatTile(valeur = "${profil.matchs}", label = "Matchs") }
        item { StatTile(valeur = "${profil.buts}", label = "Buts") }
        item { StatTile(valeur = "${profil.passes}", label = "Passes") }
        item { StatTile(valeur = formatDecimal(profil.butsParMatch), label = "Buts / match") }
        item { StatTile(valeur = "${profil.victoires}", label = "Victoires") }
        item { StatTile(valeur = "${profil.hommeDuMatch}", label = "Homme du match") }

        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Column(modifier = Modifier.padding(top = PDZSpacing.space4)) {
                Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
                    Text(text = "Buts par mois", style = MaterialTheme.typography.titleMedium)
                    profil.variationVsAnneePrecedente?.let {
                        val signe = if (it >= 0) "+" else ""
                        Text(text = "$signe${(it * 100).toInt()} % vs l'an dernier", style = MaterialTheme.typography.labelMedium, color = PDZColor.Succes)
                    }
                }
                Spacer(Modifier.height(PDZSpacing.space2))
                ProgressionMensuelle(progressionMensuelle = profil.progressionMensuelle)
                Spacer(Modifier.height(PDZSpacing.space6))
                Text(text = "Tous les badges", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(PDZSpacing.space2))
                BadgesGrid(obtenus = profil.badgesObtenus, verrouilles = profil.badgesVerrouilles)
            }
        }
    }
}

@Composable
private fun StatTile(valeur: String, label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(PDZSpacing.space3),
    ) {
        Text(text = valeur, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ProgressionMensuelle(progressionMensuelle: List<MoisValeur>) {
    val max = (progressionMensuelle.maxOfOrNull { it.valeur } ?: 1).coerceAtLeast(1)
    Row(modifier = Modifier.fillMaxWidth().height(60.dp), horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space1)) {
        progressionMensuelle.forEach { mois ->
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                val hauteur = (48 * mois.valeur / max).dp
                Spacer(Modifier.weight(1f))
                Box(modifier = Modifier.fillMaxWidth().height(hauteur).background(PDZColor.Gazon))
                Text(text = mois.mois, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun BadgesGrid(obtenus: List<BadgeObtenu>, verrouilles: List<TypeBadge>) {
    Row(horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
        obtenus.forEach { BadgeChip(label = it.type.name, obtenu = true) }
        verrouilles.forEach { BadgeChip(label = it.name, obtenu = false) }
    }
}

@Composable
private fun BadgeChip(label: String, obtenu: Boolean) {
    Text(
        text = label.replace('_', ' '),
        style = MaterialTheme.typography.labelSmall,
        color = if (obtenu) PDZColor.Blanc else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .background(if (obtenu) PDZColor.Ligne else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(horizontal = PDZSpacing.space2, vertical = PDZSpacing.space1),
    )
}

private fun formatDecimal(value: Double): String {
    val rounded = (value * 100).toInt() / 100.0
    return rounded.toString().take(4)
}
