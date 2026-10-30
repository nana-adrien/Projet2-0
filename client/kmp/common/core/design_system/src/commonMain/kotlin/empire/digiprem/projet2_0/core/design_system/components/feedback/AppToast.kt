package empire.digiprem.projet2_0.core.design_system.components.feedback

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import empire.digiprem.projet2_0.core.design_system.theme.PDZColor
import empire.digiprem.projet2_0.core.design_system.theme.PDZShape
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing
import empire.digiprem.projet2_0.core.design_system.theme.pdzToastElevation

/**
 * Toast : fond bitume, au-dessus de la barre d'onglets. Durée pilotée par l'appelant
 * (10 s si [actionLabel] est fourni pour permettre l'annulation, sinon 4 s).
 */
@Composable
fun PDZToast(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .pdzToastElevation(),
        color = PDZColor.Bitume,
        shape = PDZShape.m,
    ) {
        Row(
            modifier = Modifier.padding(
                start = PDZSpacing.space4,
                end = if (actionLabel != null) PDZSpacing.space2 else PDZSpacing.space4,
                top = PDZSpacing.space3,
                bottom = PDZSpacing.space3,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = message,
                color = PDZColor.Blanc,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f, fill = false),
            )
            if (actionLabel != null && onActionClick != null) {
                TextButton(onClick = onActionClick) {
                    Text(text = actionLabel, color = PDZColor.Ligne)
                }
            }
        }
    }
}
