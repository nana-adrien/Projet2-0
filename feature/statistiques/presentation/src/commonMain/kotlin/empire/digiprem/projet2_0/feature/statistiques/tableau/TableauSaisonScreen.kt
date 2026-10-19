package empire.digiprem.projet2_0.feature.statistiques.tableau

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButton
import empire.digiprem.projet2_0.core.design_system.components.buttons.PDZButtonType
import empire.digiprem.projet2_0.core.design_system.components.dialog.PDZConfirmDialog
import empire.digiprem.projet2_0.core.design_system.components.feedback.PDZToast
import empire.digiprem.projet2_0.core.design_system.components.season.SeasonBoard
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.textfields.PDZTextField
import empire.digiprem.projet2_0.core.design_system.components.topbar.PDZTopBar
import empire.digiprem.projet2_0.core.design_system.extension.asString
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.Saison
import empire.digiprem.projet2_0.feature.deuxzero.model.SaisonStatut
import empire.digiprem.projet2_0.feature.statistiques.model.MoisResultat
import empire.digiprem.projet2_0.feature.statistiques.model.Serie
import empire.digiprem.projet2_0.feature.statistiques.model.VictoireRecord
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TableauSaisonRoot(
    deuxZeroId: String,
    onVoirBilan: (saisonId: String) -> Unit = {},
    viewModel: TableauSaisonViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(deuxZeroId) { viewModel.onAction(TableauSaisonAction.OnInit(deuxZeroId)) }

    TableauSaisonScreen(state = state, onAction = viewModel::onAction, onVoirBilan = onVoirBilan)
}

/** E17 — Tableau de la saison (CU22). CU12 — ouvrir/clôturer une saison (organisateur uniquement). */
@Composable
fun TableauSaisonScreen(
    state: TableauSaisonState,
    onAction: (TableauSaisonAction) -> Unit,
    modifier: Modifier = Modifier,
    onVoirBilan: (saisonId: String) -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                PDZTopBar(
                    title = "Tableau de la saison",
                    actions = {
                        if (state.saisons.size > 1) {
                            SaisonSelector(
                                saisons = state.saisons,
                                saisonSelectionneeId = state.saisonSelectionneeId,
                                onSaisonChange = { onAction(TableauSaisonAction.OnSaisonChange(it)) },
                            )
                        }
                    },
                )
            },
        ) { padding ->
            when {
                state.isLoading -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize())

                else -> LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = PaddingValues(PDZSpacing.space4),
                ) {
                    item {
                        Text(text = state.tableau?.saisonLibelle.orEmpty(), style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(PDZSpacing.space4))
                        state.tableau?.let { tableau ->
                            SeasonBoard(
                                libelleA = state.campA?.libelle.orEmpty(),
                                libelleB = state.campB?.libelle.orEmpty(),
                                scoreA = tableau.scoreCampA,
                                scoreB = tableau.scoreCampB,
                            )
                            Spacer(Modifier.height(PDZSpacing.space2))
                            Text(
                                text = "${tableau.nulsCount} nuls · ${tableau.butsCampA} buts à ${tableau.butsCampB}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Spacer(Modifier.height(PDZSpacing.space6))
                    }

                    state.tableau?.bilanMensuel?.let { bilanMensuel ->
                        item {
                            Text(text = "Mois par mois", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(PDZSpacing.space2))
                            BandeMensuelle(bilanMensuel = bilanMensuel, campAId = state.campA?.id, campBId = state.campB?.id)
                            Spacer(Modifier.height(PDZSpacing.space2))
                            Text(
                                text = bilanMensuelLegende(bilanMensuel, state.campA, state.campB),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.height(PDZSpacing.space6))
                        }
                    }

                    item {
                        Text(text = "Séries et records", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(PDZSpacing.space2))
                        state.tableau?.serieEnCours?.let { PaireCleValeur(cle = "Série en cours", valeur = it.toLibelle(state)) }
                        state.tableau?.plusLongueSerie?.let { PaireCleValeur(cle = "Plus longue série", valeur = it.toLibelle(state)) }
                        state.tableau?.plusLargeVictoire?.let { PaireCleValeur(cle = "Plus large victoire", valeur = it.toLibelle(state)) }
                    }

                    val saisonSelectionnee = state.saisons.firstOrNull { it.id == state.saisonSelectionneeId }
                    if (saisonSelectionnee?.statut == SaisonStatut.CLOTUREE) {
                        item {
                            Spacer(Modifier.height(PDZSpacing.space6))
                            PDZButton(
                                text = "Voir le bilan de la saison",
                                type = PDZButtonType.Secondary,
                                onClick = { onVoirBilan(saisonSelectionnee.id) },
                            )
                        }
                    }

                    if (state.peutGererSaisons) {
                        item {
                            Spacer(Modifier.height(PDZSpacing.space6))
                            Text(text = "Gestion de la saison", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(PDZSpacing.space2))
                            if (saisonSelectionnee?.statut == SaisonStatut.EN_COURS) {
                                PDZButton(
                                    text = "Clôturer la saison",
                                    type = PDZButtonType.Secondary,
                                    isLoading = state.isSaving,
                                    onClick = { onAction(TableauSaisonAction.OnCloturerSaisonClick) },
                                )
                                Spacer(Modifier.height(PDZSpacing.space3))
                            }
                            PDZButton(
                                text = "Ouvrir une nouvelle saison",
                                type = PDZButtonType.Text,
                                isLoading = state.isSaving,
                                onClick = { onAction(TableauSaisonAction.OnOuvrirSaisonClick) },
                            )
                        }
                    }
                }
            }
        }

        state.message?.let { message ->
            LaunchedEffect(message) {
                delay(3_000)
                onAction(TableauSaisonAction.OnMessageShown)
            }
            PDZToast(
                message = message.asString(),
                modifier = Modifier.align(Alignment.BottomCenter).padding(PDZSpacing.space4),
            )
        }
    }

    if (state.showOuvrirDialog) {
        OuvrirSaisonDialog(
            onDismiss = { onAction(TableauSaisonAction.OnDismissOuvrirDialog) },
            onConfirm = { libelle, dateDebut, dateFin -> onAction(TableauSaisonAction.OnConfirmOuvrirSaison(libelle, dateDebut, dateFin)) },
        )
    }

    if (state.showCloturerDialog) {
        PDZConfirmDialog(
            title = "Clôturer la saison ?",
            message = "Les résultats seront figés (RG-18), le camp champion calculé, et le bilan de saison généré. Cette action est irréversible.",
            onConfirm = { onAction(TableauSaisonAction.OnConfirmCloturerSaison) },
            onCancel = { onAction(TableauSaisonAction.OnDismissCloturerDialog) },
            onDismiss = { onAction(TableauSaisonAction.OnDismissCloturerDialog) },
            confirmText = "Clôturer",
            cancelText = "Annuler",
        )
    }
}

@Composable
private fun OuvrirSaisonDialog(onDismiss: () -> Unit, onConfirm: (libelle: String, dateDebut: String, dateFin: String?) -> Unit) {
    var libelle by remember { mutableStateOf("") }
    var dateDebut by remember { mutableStateOf("") }
    var dateFin by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nouvelle saison") },
        text = {
            Column {
                PDZTextField(value = libelle, onValueChange = { libelle = it }, label = "Libellé", placeholder = "Saison 2026-2027", modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(PDZSpacing.space3))
                PDZTextField(value = dateDebut, onValueChange = { dateDebut = it }, label = "Date de début", placeholder = "AAAA-MM-JJ", modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(PDZSpacing.space3))
                PDZTextField(value = dateFin, onValueChange = { dateFin = it }, label = "Date de fin (facultatif)", placeholder = "AAAA-MM-JJ", modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(libelle, dateDebut, dateFin.ifBlank { null }) },
                enabled = libelle.isNotBlank() && dateDebut.isNotBlank(),
            ) { Text("Ouvrir") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annuler") } },
    )
}

/** Sélecteur de saison dans la barre d'app (E17, CU22) : les saisons autres que la saison en
 * cours restent consultables mais sont annotées « lecture seule » — leurs actions de gestion
 * (clôturer/ouvrir) ne sont jamais proposées pour elles (voir [TableauSaisonScreen]). */
@Composable
private fun SaisonSelector(saisons: List<Saison>, saisonSelectionneeId: String, onSaisonChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val saisonSelectionnee = saisons.firstOrNull { it.id == saisonSelectionneeId }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(text = saisonSelectionnee?.libelle.orEmpty(), style = MaterialTheme.typography.labelLarge)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            saisons.forEach { saison ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (saison.statut == SaisonStatut.EN_COURS) saison.libelle else "${saison.libelle} · lecture seule",
                        )
                    },
                    onClick = { expanded = false; onSaisonChange(saison.id) },
                )
            }
        }
    }
}

/** Légende de la bande mensuelle (E17, CU22) : compte les mois gagnés par chaque camp et les égalités. */
private fun bilanMensuelLegende(bilanMensuel: List<MoisResultat>, campA: Camp?, campB: Camp?): String {
    val victoiresA = bilanMensuel.count { it.campVainqueurId == campA?.id }
    val victoiresB = bilanMensuel.count { it.campVainqueurId == campB?.id }
    val egalites = bilanMensuel.count { it.campVainqueurId == null }
    val egaliteTexte = when (egalites) {
        0 -> "aucun mois à égalité"
        1 -> "un mois à égalité"
        else -> "$egalites mois à égalité"
    }
    return "${campA?.libelle.orEmpty()} ont gagné $victoiresA mois, ${campB?.libelle.orEmpty()} $victoiresB, $egaliteTexte."
}

@Composable
private fun BandeMensuelle(bilanMensuel: List<MoisResultat>, campAId: String?, campBId: String?) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PDZSpacing.space1)) {
        bilanMensuel.forEach { mois ->
            val couleur = when (mois.campVainqueurId) {
                campAId -> PDZColor.CampA
                campBId -> PDZColor.CampB
                else -> PDZColor.Gris300
            }
            Box(modifier = Modifier.size(28.dp).background(couleur))
        }
    }
}

@Composable
private fun PaireCleValeur(cle: String, valeur: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = cle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = valeur, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun Serie.toLibelle(state: TableauSaisonState): String {
    val libelleCamp = if (campId == state.campA?.id) state.campA?.libelle else state.campB?.libelle
    return "${libelleCamp.orEmpty()}, $longueur victoires"
}

private fun VictoireRecord.toLibelle(state: TableauSaisonState): String {
    val libelleCamp = if (campId == state.campA?.id) state.campA?.libelle else state.campB?.libelle
    return "${libelleCamp.orEmpty()} $scoreCampA – $scoreCampB, $date"
}
