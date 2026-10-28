package empire.digiprem.projet2_0.server.notifications.service.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter



object DateFormatter {

    fun formatInstance(time: Instant): String{

        // On le convertit dans le fuseau horaire souhaité (ex: Europe/Paris ou ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault())
        val formattedTime =  formatter.format(time) // Donne "09:06"
        return formattedTime
    }
}