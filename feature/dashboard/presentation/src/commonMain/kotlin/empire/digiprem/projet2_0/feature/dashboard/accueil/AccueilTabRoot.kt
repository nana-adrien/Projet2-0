package empire.digiprem.projet2_0.feature.dashboard.accueil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import empire.digiprem.projet2_0.feature.calendrier.fiche_match.FicheMatchRoot
import empire.digiprem.projet2_0.feature.calendrier.model.Match

@Composable
fun AccueilTabRoot() {
    var matchSelectionne by remember { mutableStateOf<Match?>(null) }

    val match = matchSelectionne
    if (match != null) {
        FicheMatchRoot(match = match)
    } else {
        AccueilRoot(onMatchClick = { matchSelectionne = it })
    }
}
