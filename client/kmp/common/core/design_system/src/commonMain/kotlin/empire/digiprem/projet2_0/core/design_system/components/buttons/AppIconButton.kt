package empire.digiprem.projet2_0.core.design_system.components.buttons

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import empire.digiprem.projet2_0.core.design_system.components.icon.PDZIcon
import empire.digiprem.projet2_0.core.design_system.components.icon.PDZIconResource

@Composable
fun PDZIconButton(
    icon: PDZIconResource,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    iconSize: Dp = 24.dp,
    enabled: Boolean = true,
) {
    IconButton(
        onClick  = onClick,
        modifier = modifier,
        enabled  = enabled,
    ) {
        PDZIcon(
            icon               = icon,
            contentDescription = contentDescription,
            tint               = tint,
            size               = iconSize,
        )
    }
}
