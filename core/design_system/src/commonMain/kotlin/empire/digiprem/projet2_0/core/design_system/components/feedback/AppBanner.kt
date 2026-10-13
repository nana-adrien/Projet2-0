package empire.digiprem.projet2_0.core.design_system.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import empire.digiprem.projet2_0.core.design_system.components.icon.PDZIcon
import empire.digiprem.projet2_0.core.design_system.components.icon.PDZIconResource
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZShape
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing

enum class PDZBannerType { Info, Offline, Success, Error }

/**
 * Bannière pleine largeur, persistante tant que l'état dure (ex. hors connexion).
 * Contrairement au Toast, elle ne se referme pas seule.
 */
@Composable
fun PDZBanner(
    message: String,
    icon: PDZIconResource,
    modifier: Modifier = Modifier,
    type: PDZBannerType = PDZBannerType.Info,
) {
    val (background, content) = when (type) {
        PDZBannerType.Info -> PDZColor.Gazon50 to PDZColor.Gazon700
        PDZBannerType.Offline -> PDZColor.AlerteFond to PDZColor.AlerteTexte
        PDZBannerType.Success -> PDZColor.Gazon50 to PDZColor.Succes
        PDZBannerType.Error -> PDZColor.Erreur.copy(alpha = 0.1f) to PDZColor.Erreur
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = background, shape = PDZShape.m)
            .padding(horizontal = PDZSpacing.space4, vertical = PDZSpacing.space3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PDZIcon(icon = icon, contentDescription = null, tint = content, size = 20.dp)
        Text(
            text = message,
            color = content,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = PDZSpacing.space2),
        )
    }
}
