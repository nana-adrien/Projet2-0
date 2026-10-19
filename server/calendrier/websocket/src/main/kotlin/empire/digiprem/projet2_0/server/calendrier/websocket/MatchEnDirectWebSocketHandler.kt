package empire.digiprem.projet2_0.server.calendrier.websocket

import empire.digiprem.projet2_0.server.calendrier.service.EvenementService
import empire.digiprem.projet2_0.server.calendrier.service.MatchEnDirectBroadcaster
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.UUID

/** CU18 — un socket par spectateur, un topic par match (URI : /ws/matchs/{matchId}). À la
 * connexion, envoie l'état courant (score + chronologie) puis reste ouvert pour les diffusions
 * de [MatchEnDirectBroadcaster]. Aucun message entrant n'est traité (lecture seule, CU17 reste
 * exclusivement via REST, saisi par le marqueur désigné). */
@Component
class MatchEnDirectWebSocketHandler(
    private val evenementService: EvenementService,
    private val broadcaster: MatchEnDirectBroadcaster,
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val matchId = matchIdFrom(session) ?: run {
            session.close(CloseStatus.BAD_DATA.withReason("matchId manquant dans l'URL"))
            return
        }
        broadcaster.register(matchId, session)
        val etatCourant = runCatching { evenementService.getMatchEnDirect(matchId) }.getOrNull()
        if (etatCourant != null) {
            broadcaster.sendTo(session, etatCourant)
        } else {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Match introuvable"))
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        broadcaster.unregister(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        // Lecture seule — aucune action attendue des messages entrants (voir note de classe).
    }

    private fun matchIdFrom(session: WebSocketSession): UUID? {
        val segments = session.uri?.path?.split("/") ?: return null
        val raw = segments.lastOrNull() ?: return null
        return runCatching { UUID.fromString(raw) }.getOrNull()
    }
}
