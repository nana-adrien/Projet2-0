package empire.digiprem.projet2_0.server.notifications.service

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service
class PushNotificationService(
    firebaseApp: FirebaseApp
) {
    var fcmInstance: FirebaseMessaging = FirebaseMessaging.getInstance(firebaseApp)

    fun sendNotificationPush(
        targetTokens: List<String>,
        title: String,
        body: String,
        data: Map<String, String>
    ) {
        try {


            targetTokens.forEach { targetToken ->

                // 1. Structure du texte de la notification
                val notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build()

                // 2. Assemblage du message Firebase ciblé
                val message = Message.builder()
                    .setToken(targetToken) // Cible l'appareil spécifique
                    .setNotification(notification)
                    .apply {
                        data.forEach { (key, value) ->
                            putData(key, value)
                        }
                    }
                    // Optionnel : Tu peux ajouter des données brutes (Data payload) lues par l'application
                    // .putData("click_action", "OPEN_SIGNUP_BADGES")
                    // .putData("route", "home_screen")
                    .build()

                // 3. Envoi au serveur Firebase Cloud Messaging
                val response = fcmInstance.send(message)
                println("FCM • Notification envoyée avec succès ! Message ID: $response")
            }
        } catch (e: Exception) {
            System.err.println("FCM • Échec de l'envoi de la notification : ${e.message}")
        }


    }
}