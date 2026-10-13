package empire.digiprem.projet2_0.core.design_system.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import empire.digiprem.projet2_0.core.design_system.components.icon.PDZIcon
import empire.digiprem.projet2_0.core.design_system.components.icon.PDZIconResource
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing

data class PDZTabBarItem(
    val icon: PDZIconResource,
    val label: String,
    val showLiveBadge: Boolean = false,
)

/**
 * TabBar : 5 onglets fixes (écrans racines), icône 24 + libellé 11pt 600.
 * Les routes/destinations sont décidées par l'appelant (composeApp) — ce composant est pur.
 */
@Composable
fun PDZTabBar(
    items: List<PDZTabBarItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .height(58.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            items.forEachIndexed { index, item ->
                PDZTabBarEntry(
                    item = item,
                    selected = index == selectedIndex,
                    onClick = { onItemSelected(index) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun PDZTabBarEntry(
    item: PDZTabBarItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tint = if (selected) PDZColor.Gazon else PDZColor.Gris400

    Column(
        modifier = modifier
            .selectable(selected = selected, onClick = onClick, role = Role.Tab)
            .padding(top = PDZSpacing.space2),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            PDZIcon(icon = item.icon, contentDescription = item.label, tint = tint, size = 24.dp)
            if (item.showLiveBadge) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(x = 6.dp, y = (-2).dp)
                        .align(Alignment.TopEnd)
                        .background(color = PDZColor.Live, shape = CircleShape),
                )
            }
        }
        Text(
            text = item.label,
            color = tint,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}
