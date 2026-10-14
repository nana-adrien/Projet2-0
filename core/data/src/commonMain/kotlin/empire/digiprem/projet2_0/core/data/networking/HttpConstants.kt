package empire.digiprem.projet2_0.core.data.networking

object HttpConstants {
    // Dev local : 10.0.2.2 = hôte vu depuis l'émulateur Android ; le simulateur iOS utilise localhost
    // directement. À remplacer par l'URL de production au déploiement.
    const val BASE_URL = "http://192.168.100.28:8085/api/"

    // Même hôte que BASE_URL, schéma ws:// et chemin /ws/ (CU18 — WebSocket brut, pas sous /api/).
    const val WS_BASE_URL = "ws://192.168.100.28:8085/ws/"

    const val ALLOW_ANONYMOUS       = "Allow-Anonymous"
    const val ALLOW_ANONYMOUS_VALUE = "1"
    const val SUBSCRIPTION_KEY      = "Subscription-Key"
    const val TENANT_ID             = "Tenant-Id"
    const val ACCEPT_LANGUAGE       = "Accept-Language"
    const val X_API_KEY             = "X-Api-Key"
    const val X_CORRELATION_ID      = "X-Correlation-Id"

    // Endpoints ne nécessitant pas de jeton (AuthController, voir server/user/.../AuthController.kt)
    private val anonymousEndpoints = setOf(
        "/auth/otp",
        "/auth/register",
        "/auth/login",
        "/auth/reset-password",
        "/auth/refresh",
    )

    fun isAnonymous(path: String): Boolean =
        anonymousEndpoints.any { path.contains(it) }
}
