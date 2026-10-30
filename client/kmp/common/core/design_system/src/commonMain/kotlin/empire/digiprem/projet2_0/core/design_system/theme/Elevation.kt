package empire.digiprem.projet2_0.core.design_system.theme

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object PDZElevation {
    val e1BorderWidth: Dp = 1.dp

    val e2Blur: Dp = 24.dp
    val e2Color: Color = Color(0x2918201D)

    val e3Blur: Dp = 32.dp
    val e3Color: Color = Color(0x3818201D)
}

fun Modifier.pdzCardBorder(
    shape: Shape = PDZShape.m,
    color: Color = PDZColor.Gris200,
): Modifier = this
    .clip(shape)
    .border(width = PDZElevation.e1BorderWidth, color = color, shape = shape)

fun Modifier.pdzSheetElevation(
    shape: Shape = PDZShape.sheet,
): Modifier = this.shadow(
    elevation = PDZElevation.e2Blur / 2,
    shape = shape,
    ambientColor = PDZElevation.e2Color,
    spotColor = PDZElevation.e2Color,
)

fun Modifier.pdzToastElevation(
    shape: Shape = PDZShape.m,
): Modifier = this.shadow(
    elevation = PDZElevation.e3Blur / 2,
    shape = shape,
    ambientColor = PDZElevation.e3Color,
    spotColor = PDZElevation.e3Color,
)
