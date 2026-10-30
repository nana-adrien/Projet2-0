package empire.digiprem.projet2_0

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.mandatorySystemGestures
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import empire.digiprem.projet2_0.core.design_system.theme.PDZTheme
import empire.digiprem.projet2_0.navigation.AppNavigation

@Composable
fun App() {
    PDZTheme {
        Box(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)){
            AppNavigation()
        }
    }
}
