package empire.digiprem.projet2_0.core.design_system.components.image

import empire.digiprem.projet2_0.core.domain.model.AppFile
import org.jetbrains.compose.resources.DrawableResource

sealed interface PDZImageSource {
    data class UrlSource(val url: String) : PDZImageSource
    data class FileSource(val file: AppFile) : PDZImageSource
    data class ResourceSource(val resource: DrawableResource) : PDZImageSource
    data object Unknown : PDZImageSource
}
