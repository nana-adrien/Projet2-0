package empire.digiprem.projet2_0.core.design_system.components.icon

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

sealed interface PDZIconResource {
    data class VectorResource(val vector: ImageVector) : PDZIconResource
    data class PainterResource(val resource: DrawableResource) : PDZIconResource
}
