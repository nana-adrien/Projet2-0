package empire.digiprem.projet2_0.core.data.networking

import co.touchlab.kermit.Logger
import empire.digiprem.projet2_0.contracts.auth.AuthResponse
import empire.digiprem.projet2_0.contracts.auth.RefreshTokenRequest
import empire.digiprem.projet2_0.core.domain.service.AuthSessionStorage
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val engine: HttpClientEngine,
    private val authSessionStorage: AuthSessionStorage,
    private val logger: Logger = Logger.withTag("HttpClient"),
) {
    fun create(): HttpClient = HttpClient(engine) {
        installContentNegotiation()

        install(HttpTimeout) {
            socketTimeoutMillis = 30_000L
            requestTimeoutMillis = 30_000L

        }
        installLogging()
        installWebSockets()
        installAuth()
    }

    private fun HttpClientConfig<*>.installContentNegotiation() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            })
        }
    }

    private fun HttpClientConfig<*>.installLogging() {
        install(Logging) {
            level = LogLevel.BODY
            this.logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) = this@HttpClientFactory.logger.d { message }
            }
        }
    }

    private fun HttpClientConfig<*>.installWebSockets() {
        install(WebSockets)
    }

    private fun HttpClientConfig<*>.installAuth() {
        install(Auth) {
            bearer {
                // Charge le jeton courant depuis le stockage sécurisé (DataStore chiffré).
                loadTokens {
                    authSessionStorage.getData()
                        ?.let { BearerTokens(it.accessToken, it.refreshToken) }
                }

                // Appelée sur un 401 : rafraîchit via /v1/auth/refresh puis persiste la nouvelle session.
                refreshTokens {
                    val current = authSessionStorage.getData() ?: return@refreshTokens null

                    val response = client.post(HttpConstants.BASE_URL + "v1/auth/refresh") {
                        markAsRefreshTokenRequest()
                        contentType(ContentType.Application.Json)
                        setBody(RefreshTokenRequest(refreshToken = current.refreshToken))
                    }

                    if (!response.status.isSuccess()) {
                        // Refresh token invalide/expiré -> déconnexion forcée (RG-01/sécurité).
                        authSessionStorage.clear()
                        return@refreshTokens null
                    }

                    val body = response.body<AuthResponse>()
                    val updated = current.copy(
                        accessToken  = body.accessToken,
                        refreshToken = body.refreshToken,
                        userId       = body.userId,
                    )
                    authSessionStorage.set(updated)
                    BearerTokens(updated.accessToken, updated.refreshToken)
                }

                // N'envoie pas de jeton (même vide) sur les endpoints publics.
                sendWithoutRequest { request ->
                    !HttpConstants.isAnonymous(request.url.encodedPath)
                }
            }
        }
    }
}
