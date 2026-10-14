package empire.digiprem.projet2_0.navigation.app

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import empire.digiprem.projet2_0.feature.dashboard.dashboard.DashboardRoot

fun NavGraphBuilder.appNavGraph(
    navController : NavHostController,
    onLogout      : () -> Unit,
) {
    navigation<AppNavGraphRoute.Application>(startDestination = AppNavGraphRoute.Dashboard) {
        composable<AppNavGraphRoute.Dashboard> {
            DashboardRoot(onLogout = onLogout)
        }
    }
}
