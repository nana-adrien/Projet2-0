package empire.digiprem.projet2_0.navigation.pre_auth

import kotlinx.serialization.Serializable

sealed interface PreAuthNavGraphRoute {
    @Serializable data object PreAuth : PreAuthNavGraphRoute
    @Serializable data object Splash  : PreAuthNavGraphRoute
}
