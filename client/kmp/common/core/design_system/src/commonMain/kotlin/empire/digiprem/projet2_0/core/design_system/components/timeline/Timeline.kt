package empire.digiprem.projet2_0.core.design_system.components.timeline

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing

enum class TimelineSide { A, B }

data class TimelineEntry(
    val id: String,
    val minute: Int,
    val title: String,
    val subtitle: String? = null,
    val side: TimelineSide,
    val highlighted: Boolean = false,
)

/** Chronologie en miroir A/B (E10 — 3 derniers événements ; E12 — chronologie complète). */
@Composable
fun Timeline(entries: List<TimelineEntry>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(PDZSpacing.space2)) {
        entries.forEach { entry -> TimelineRow(entry) }
    }
}

@Composable
private fun TimelineRow(entry: TimelineEntry) {
    val backgroundColor by animateColorAsState(
        targetValue = if (entry.highlighted) PDZColor.AlerteFond else PDZColor.Blanc,
    )
    val campColor = if (entry.side == TimelineSide.A) PDZColor.CampA else PDZColor.CampB
    val alignment = if (entry.side == TimelineSide.A) Alignment.Start else Alignment.End
    val textAlign = if (entry.side == TimelineSide.A) TextAlign.Start else TextAlign.End

    Row(
        modifier = Modifier.fillMaxWidth().background(backgroundColor).padding(PDZSpacing.space2),
        horizontalArrangement = if (entry.side == TimelineSide.A) Arrangement.Start else Arrangement.End,
    ) {
        Column(horizontalAlignment = alignment) {
            Text(
                text = "${entry.minute}' ${entry.title}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = campColor,
                textAlign = textAlign,
            )
            entry.subtitle?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = textAlign)
            }
        }
    }
}
