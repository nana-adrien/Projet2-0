package empire.digiprem.projet2_0.core.design_system.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object PDZRadius {
    val s: Dp = 6.dp
    val m: Dp = 12.dp
    val l: Dp = 16.dp
    val sheet: Dp = 20.dp
    val pill: Dp = 999.dp
}

object PDZShape {
    val s: Shape = RoundedCornerShape(PDZRadius.s)
    val m: Shape = RoundedCornerShape(PDZRadius.m)
    val l: Shape = RoundedCornerShape(PDZRadius.l)
    val sheet: Shape = RoundedCornerShape(topStart = PDZRadius.sheet, topEnd = PDZRadius.sheet)
    val pill: Shape = RoundedCornerShape(PDZRadius.pill)
}
