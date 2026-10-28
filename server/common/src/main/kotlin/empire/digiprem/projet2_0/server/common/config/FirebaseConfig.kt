package empire.digiprem.projet2_0.server.common.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class FirebaseConfig {

    @Bean
    fun initializeFirebase(): FirebaseApp{
        try{
            // Réutilise l'application Firebase par défaut si elle existe.
            FirebaseApp.getApps()
                .firstOrNull { it.name == FirebaseApp.DEFAULT_APP_NAME }
                ?.let { return it }


            val serviceAccount = ClassPathResource("Firebase-service-account.json").inputStream

            val options= FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

            if (FirebaseApp.getApps().isEmpty()){
              return  FirebaseApp.initializeApp(options)
                println("FCM • Le serveur s'est connecté à Firebase avec succès. ⚽")
            } else   error("Impossible d'initialiser FirebaseApp.")

        }catch (e: IOException) {
            System.err.println("FCM • Erreur d'initialisation de Firebase Admin SDK : ${e.message}")

            return  error("Impossible d'initialiser FirebaseApp.")
        }
    }
}