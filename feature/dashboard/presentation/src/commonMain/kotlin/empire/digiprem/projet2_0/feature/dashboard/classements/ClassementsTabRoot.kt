package empire.digiprem.projet2_0.feature.dashboard.classements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.feature.statistiques.bilan.BilanRoot
import empire.digiprem.projet2_0.feature.statistiques.classement.ClassementRoot
import empire.digiprem.projet2_0.feature.statistiques.profil.ProfilRoot
import empire.digiprem.projet2_0.feature.statistiques.tableau.TableauSaisonRoot
import org.koin.compose.viewmodel.koinViewModel

private enum class ClassementsVue { SAISON, CLASSEMENT }

private sealed interface ClassementsTabScreen {
    data object Accueil : ClassementsTabScreen
    data class Profil(val utilisateurId: String) : ClassementsTabScreen
    data class Bilan(val saisonId: String) : ClassementsTabScreen
}

@Composable
fun ClassementsTabRoot(viewModel: ClassementsTabViewModel = koinViewModel()) {
    val deuxZeroId by viewModel.deuxZeroId.collectAsStateWithLifecycle()
    var screen by remember { mutableStateOf<ClassementsTabScreen>(ClassementsTabScreen.Accueil) }
    var vue by remember { mutableStateOf(ClassementsVue.SAISON) }

    val currentDeuxZeroId = deuxZeroId
    if (currentDeuxZeroId == null) {
        Scaffold { padding -> PDZLoadingSpinner(modifier = Modifier.padding(padding).fillMaxSize()) }
        return
    }

    when (val current = screen) {
        ClassementsTabScreen.Accueil -> Scaffold { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                TabRow(selectedTabIndex = ClassementsVue.entries.indexOf(vue)) {
                    Tab(selected = vue == ClassementsVue.SAISON, onClick = { vue = ClassementsVue.SAISON }, text = { Text("Saison") })
                    Tab(selected = vue == ClassementsVue.CLASSEMENT, onClick = { vue = ClassementsVue.CLASSEMENT }, text = { Text("Classement") })
                }
                when (vue) {
                    ClassementsVue.SAISON -> TableauSaisonRoot(
                        deuxZeroId = currentDeuxZeroId,
                        onVoirBilan = { saisonId -> screen = ClassementsTabScreen.Bilan(saisonId) },
                    )
                    ClassementsVue.CLASSEMENT -> ClassementRoot(
                        deuxZeroId = currentDeuxZeroId,
                        onJoueurClick = { utilisateurId -> screen = ClassementsTabScreen.Profil(utilisateurId) },
                    )
                }
            }
        }

        is ClassementsTabScreen.Profil -> ProfilRoot(
            deuxZeroId = currentDeuxZeroId,
            utilisateurId = current.utilisateurId,
        )

        is ClassementsTabScreen.Bilan -> BilanRoot(
            saisonId = current.saisonId,
            deuxZeroId = currentDeuxZeroId,
        )
    }
}
