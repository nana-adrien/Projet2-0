package empire.digiprem.projet2_0.server.user.service

import empire.digiprem.projet2_0.contracts.config.ConnectedDeviceResponse
import empire.digiprem.projet2_0.contracts.config.InitialDeviceConfigRequest
import empire.digiprem.projet2_0.server.user.infra.entity.UserDeviceConfigEntity
import empire.digiprem.projet2_0.server.user.infra.repository.UserDeviceConfigRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID
import kotlin.time.toKotlinInstant

@Service
class DeviceConfigService(
    private val configRepository: UserDeviceConfigRepository,
    private val userRepository: UserRepository
) {


    /**
     * Synchronise le contexte technique de l'appareil connecté.
     * Applique la limite stricte de 3 appareils maximum par joueur.
     */

    @Transactional
    fun synchronizeDevice(userId: UUID, request: InitialDeviceConfigRequest) {
        // 1. SÉCURITÉ NETTOYAGE : Si ce jeton Firebase précis existe sur un autre compte, on le supprime

        configRepository.deleteByToken(request.pushToken)

        // 2. RECHERCHE PAR IDENTIFIANT MATÉRIEL UNIQUE

        val user = userRepository.findById(userId)
            .orElseThrow { throw RuntimeException("User Not Found ") }

        val existingConfig = configRepository.findByDeviceId(request.deviceId)

        if (existingConfig != null) {
            // CASE A : L'appareil est connu. On rafraîchit ses données mutables (Token, OS, Version)
            existingConfig.token = request.pushToken
            existingConfig.user = user // Gère le changement de compte sur le même téléphone
            existingConfig.appVersion = request.appVersion
            existingConfig.timezone = request.timezone
            existingConfig.lastUsedAt = Instant.now() // Marqueur d'activité

            configRepository.save(existingConfig)
            println("FCM • Appareil connu [${request.deviceId}] mis à jour pour le joueur $userId. ⏱️")

        } else {

            // CASE B : C'est un nouvel appareil physique pour ce joueur.
            // On applique la BARRIÈRE DES 3 APPAREILS MAXIMUM (Style Netflix)

            val activeDevices = configRepository.findAllByUserIdOrderByLastUsedAtAsc(userId)

            if (activeDevices.size >= 3) {
                // On isole le plus ancien (le premier de la liste triée par date ascendante)
                val oldestDevice = activeDevices.first()
                configRepository.delete(oldestDevice)
                println("FCM • Sécurité [Limite Max] : Suppression de l'appareil inactif le plus ancien [${oldestDevice.deviceId}] pour le joueur $userId. 🧹")
            }
            // Enregistrement du nouvel appareil physique en toute sécurité
            val newConfig = UserDeviceConfigEntity(
                user = user,
                deviceId = request.deviceId,
                token = request.pushToken,
                platform = request.platform,
                appVersion = request.appVersion,
                timezone = request.timezone,
                lastUsedAt = Instant.now()
            )
            configRepository.save(newConfig)
            println("FCM • Nouvel appareil [${request.deviceId}] enregistré avec succès pour le joueur $userId. 📱")


        }


    }

    /**
     * DÉCONNEXION À DISTANCE / RÉVOCATION D'APPAREIL
     */
    @Transactional
    fun revokeDevice(userId: UUID, deviceId: String) {
        configRepository.deleteByUserIdAndDeviceId(userId, deviceId)
        println("FCM • Sécurité : L'appareil [$deviceId] a été révoqué par le joueur $userId. ❌")
    }

    /**
     * Liste tous les appareils d'un utilisateur convertis en DTO de réponse.
     */
    @Transactional
    fun getDevicesByUserId(userId: UUID): List<ConnectedDeviceResponse> {
        // Récupère les configurations actives du joueur depuis le repository
        return configRepository.findAllByUserIdOrderByLastUsedAtAsc(userId).map { entity ->
            ConnectedDeviceResponse(
                deviceId = entity.deviceId,
                platform = entity.platform,
                appVersion = entity.appVersion,
                timezone = entity.timezone,
                lastUsedAt = entity.lastUsedAt.toKotlinInstant()
            )
        }
    }

    @Transactional
    fun updateTokenOnly(userId: UUID, deviceId: String, newPushToken: String) {
        // 1. On cherche si l'appareil est déjà enregistré
        val deviceConfig = configRepository.findByDeviceId(deviceId)

        val user = userRepository.findById(userId)
            .orElseThrow { throw RuntimeException("User Not Found ") }

        if (deviceConfig != null) {
            // OPTIMISATION : Si l'appareil, l'utilisateur et le jeton sont DÉJÀ identiques
            if (deviceConfig.user.id == userId && deviceConfig.token == newPushToken) {
                deviceConfig.lastUsedAt = Instant.now() // On met juste à jour le marqueur d'activité
                configRepository.save(deviceConfig)
                println("FCM • Optimisation : Rien n'a changé pour l'appareil [$deviceId]. Simple mise à jour de l'heure. ⏱️")
                return // 👈 ON S'ARRÊTE ICI. Le mécanisme de sécurité ne s'exécute pas.
            }
        }

        // 2. Le mécanisme de sécurité ne s'exécute QUE si le jeton ou l'utilisateur a changé
        configRepository.deleteByToken(newPushToken)

        if (deviceConfig != null) {
            deviceConfig.token = newPushToken
            deviceConfig.user = user // Gère le changement de compte sur le même téléphone
            deviceConfig.lastUsedAt = Instant.now()
            configRepository.save(deviceConfig)
            println("FCM • Sécurité : Jeton ou utilisateur modifié pour l'appareil [$deviceId]. Enregistré. 🔄")
        } else {
            System.err.println("FCM • Erreur : Tentative de mise à jour de jeton sur un appareil inconnu [$deviceId].")
        }
    }
}

