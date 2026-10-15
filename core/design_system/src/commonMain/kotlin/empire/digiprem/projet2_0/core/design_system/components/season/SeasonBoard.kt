package empire.digiprem.projet2_0.core.design_system.components.season

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor

/** Tableau de score réutilisé à l'échelle du match (E10/E12) ou de la saison (E17/E21/E05). */
@Composable
fun SeasonBoard(
    libelleA: String,
    libelleB: String,
    scoreA: Int,
    scoreB: Int,
    modifier: Modifier = Modifier,
    champion: String? = null,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = libelleA,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (champion == libelleA) FontWeight.Bold else FontWeight.Normal,
            color = PDZColor.CampA,
        )
        Text(
            text = "$scoreA – $scoreB",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = libelleB,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (champion == libelleB) FontWeight.Bold else FontWeight.Normal,
            color = PDZColor.CampB,
        )
    }
}
