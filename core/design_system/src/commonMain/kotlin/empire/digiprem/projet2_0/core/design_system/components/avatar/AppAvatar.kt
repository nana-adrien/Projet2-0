package empire.digiprem.projet2_0.core.design_system.components.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor

/**
 * Avatar : 40 (liste), 32 (compact), 72 (profil). Initiales sur fond gazon 100. Anneau de 2 pt à
 * la couleur du camp quand le contexte est un match (sinon [ringColor] = null).
 */
@Composable
fun AppAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    ringColor: Color? = null,
) {
    Box(
        modifier = modifier
            .size(size)
            .let { if (ringColor != null) it.border(width = 2.dp, color = ringColor, shape = CircleShape) else it }
            .background(color = PDZColor.Gazon100, shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials.take(2).uppercase(),
            color = PDZColor.Gazon700,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
