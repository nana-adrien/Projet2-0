package empire.digiprem.projet2_0.server.calendrier.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

/** CU18 — un socket brut par match (pas de STOMP : le client mobile Ktor consomme des frames
 * texte JSON directement). L'authentification passe par le même filtre JWT que le reste de
 * l'API (en-tête Authorization envoyé pendant la négociation du handshake). */
@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val handler: MatchEnDirectWebSocketHandler,
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(handler, "/ws/matchs/*").setAllowedOriginPatterns("*")
    }
}
