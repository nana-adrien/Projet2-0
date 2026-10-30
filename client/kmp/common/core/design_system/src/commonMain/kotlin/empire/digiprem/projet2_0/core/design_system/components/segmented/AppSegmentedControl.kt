package empire.digiprem.projet2_0.core.design_system.components.segmented

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZShape

/**
 * Contrôle segmenté (Segmented, cf. système de design) : conteneur gris 200, rayon 10, padding 3 ;
 * segments de 34 pt, Barlow 600 14 pt, segment actif blanc avec ombre fine. 2 à 4 segments.
 */
@Composable
fun PDZSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(options.size in 2..4) { "PDZSegmentedControl accepte 2 à 4 segments (${options.size} fournis)." }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(PDZColor.Gris200, PDZShape.m)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(PDZShape.s)
                    .let { if (selected) it.shadow(1.dp, PDZShape.s) else it }
                    .background(if (selected) PDZColor.Blanc else Color.Transparent)
                    .clickable { onOptionSelected(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (selected) PDZColor.Bitume else PDZColor.Gris600,
                )
            }
        }
    }
}
