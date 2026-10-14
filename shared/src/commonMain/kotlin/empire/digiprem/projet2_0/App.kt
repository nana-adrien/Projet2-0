package empire.digiprem.projet2_0

import androidx.compose.runtime.Composable
import empire.digiprem.projet2_0.core.design_system.theme.PDZTheme
import empire.digiprem.projet2_0.navigation.AppNavigation

@Composable
fun App() {
    PDZTheme {
        AppNavigation()
    }
}
