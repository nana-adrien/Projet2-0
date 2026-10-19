package empire.digiprem.projet2_0.server.calendrier.service

import com.fasterxml.jackson.databind.ObjectMapper
import empire.digiprem.projet2_0.contracts.matchendirect.MatchEnDirectDto
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/** CU18 — diffuse l'état d'un match en direct (score, chronologie) à tous les sockets ouverts
 * sur ce match (joueurs, supporters). Diffusion en mémoire uniquement : limite assumée pour une
 * seule instance serveur (pas de Redis/pub-sub au Jalon 3, cf. architecture technique). */
@Service
class MatchEnDirectBroadcaster(
    private val objectMapper: ObjectMapper,
) {
    private val sessionsParMatch = ConcurrentHashMap<UUID, MutableSet<WebSocketSession>>()

    fun register(matchId: UUID, session: WebSocketSession) {
        sessionsParMatch.computeIfAbsent(matchId) { ConcurrentHashMap.newKeySet() }.add(session)
    }

    fun unregister(session: WebSocketSession) {
        sessionsParMatch.values.forEach { it.remove(session) }
    }

    fun broadcast(matchId: UUID, payload: MatchEnDirectDto) {
        val sessions = sessionsParMatch[matchId] ?: return
        val message = TextMessage(objectMapper.writeValueAsString(payload))
        sessions.filter { it.isOpen }.forEach { session ->
            runCatching { session.sendMessage(message) }
        }
    }

    /** Envoie l'état initial au seul socket qui vient de se connecter (pas aux autres spectateurs). */
    fun sendTo(session: WebSocketSession, payload: MatchEnDirectDto) {
        runCatching { session.sendMessage(TextMessage(objectMapper.writeValueAsString(payload))) }
    }
}
