package empire.digiprem.projet2_0.core.design_system.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import empire.digiprem.projet2_0.core.design_system.theme.PDZSpacing

/** Ligne de liste : hauteur mini 56, titre + sous-titre optionnel, contenu de fin libre (valeur, chevron...). */
@Composable
fun AppListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(horizontal = PDZSpacing.space4, vertical = PDZSpacing.space2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading?.let {
            it()
            Spacer(Modifier.padding(start = PDZSpacing.space3))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            subtitle?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        trailing?.invoke(this)
    }
}
