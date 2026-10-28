package empire.digiprem.projet2_0.server.common.event

import java.time.Instant
import java.util.UUID

/**
 * Publié par le module comptes à la création d'un compte (CU1, étape 4) quand un code de
 * club a été saisi à l'inscription. Le module clubs écoute cet événement pour déclencher
 * CU6 (rejoindre un club) sans dépendance directe comptes -> clubs.
 */
data class UserRegisteredEvent(
    val userId: UUID,
    val clubCode: String?,
)

data class SendOTPEvent(
    val contact: String,
    val otp: String,
    val reason: SendOTPReason,
    val isEmail: Boolean,
    val expiresAt: Instant,
){
    enum class SendOTPReason{
        SIGNUP, LOGIN, PASSWORD_RESET
    }
}
data class SendNotificationEvent(
   val userId: UUID,
   val title: String,
   val body: String,
   val data: Map<String, String> = emptyMap()
)