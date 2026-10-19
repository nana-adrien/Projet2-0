package empire.digiprem.projet2_0.server.clubs.service

import empire.digiprem.projet2_0.server.common.event.UserRegisteredEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

/** CU1 étape 4 -> CU6 : un code de club saisi à l'inscription déclenche la demande d'adhésion. */
@Component
class ClubEventListener(
    private val clubService: ClubService,
) {
    @Async
    @EventListener
    fun onUserRegistered(event: UserRegisteredEvent) {
        val code = event.clubCode ?: return
        clubService.requestJoinByCode(event.userId, code)
    }
}
