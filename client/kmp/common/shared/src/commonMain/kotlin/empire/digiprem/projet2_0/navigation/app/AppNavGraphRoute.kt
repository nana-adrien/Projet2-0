package empire.digiprem.projet2_0.navigation.app

import kotlinx.serialization.Serializable

sealed interface AppNavGraphRoute {
    @Serializable data object Application : AppNavGraphRoute
    @Serializable data object Dashboard   : AppNavGraphRoute
}
