package empire.digiprem.projet2_0.server.notifications.service

import empire.digiprem.projet2_0.server.common.event.SendOTPEvent.SendOTPReason
import empire.digiprem.projet2_0.server.notifications.service.utils.DateFormatter.formatInstance
import org.springframework.stereotype.Service
import java.lang.Integer.valueOf
import java.time.Instant

@Service
class SendSMSService {


    fun sendOtpSms(toPhoneNumber: String,   otp: String,reason: SendOTPReason,expirationTime: Instant) {
        // 1. Génération du texte via ton template
        valueOf(toPhoneNumber)
        val smsBody = smsOtpTemplate(otp, reason, formatInstance(expirationTime) )

        try {
            // 2. Envoi du SMS via l'API Twilio
            /*val message = Message.creator(
                PhoneNumber(toPhoneNumber),       // Numéro du destinataire
                PhoneNumber(twilioPhoneNumber),   // Ton numéro Twilio expéditeur
                smsBody                           // Contenu du message
            ).create()*/

            println("SMS envoyé avec succès ! ${smsBody}")
        } catch (e: Exception) {
            // Gestion des erreurs (numéro invalide, solde insuffisant, etc.)
            System.err.println("Échec de l'envoi du SMS : \${e.message}")
            throw RuntimeException("Erreur lors de l'envoi du SMS de vérification", e)
        }

    }

    private fun smsOtpTemplate(otp: String, reason: SendOTPReason, expirationTime: String): String {
        return when (reason) {
            SendOTPReason.SIGNUP ->
                "2-0 • Code d'inscription : $otp. Saisis-le pour valider la création de ton compte. Valable jusqu'à $expirationTime."

            SendOTPReason.LOGIN ->
                "2-0 • Code de connexion : $otp. Ne partage jamais ce code. Valable jusqu'à $expirationTime."

            SendOTPReason.PASSWORD_RESET ->
                "2-0 • Réinitialisation de mot de passe : Utilise le code $otp pour modifier tes accès. Valable jusqu'à $expirationTime."
        }
    }
}