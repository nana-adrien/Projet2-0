package empire.digiprem.projet2_0.server.user.service

import empire.digiprem.projet2_0.server.user.infra.repository.UserDeviceConfigRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class TokenCleanupScheduler(
    private val userRepository: UserRepository,
    private val tokenRepository: UserDeviceConfigRepository
) {

    // S'exécute par exemple tous les jours à 02h00 du matin (Format CRON)
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    fun cleanupAbandonedTokens() {
        println("FCM • Lancement de la purge automatique des jetons obsolètes... ⚙️")

        val limiteInactivite = Instant.now().minus(30, ChronoUnit.DAYS)
        val allUsers = userRepository.findAll()

        for (user in allUsers) {
            // Nettoyage pour chaque joueur de l'application
            tokenRepository.deleteOldTokens(user.id!!, limiteInactivite)
        }

        println("FCM • Fin de la purge. Base de données optimisée pour les prochains matchs ! ⚽")
    }
}
