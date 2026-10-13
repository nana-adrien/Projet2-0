package empire.digiprem.projet2_0.core.design_system.extension

import androidx.compose.runtime.Composable
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import org.jetbrains.compose.resources.stringResource

@Composable
fun UiText.asString(): String = when (this) {
    is UiText.DynamicString -> value
    is UiText.Resource      -> stringResource(id, *args)
}
