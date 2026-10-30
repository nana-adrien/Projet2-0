package empire.digiprem.projet2_0.core.domain.util

/** Date du jour au format AAAA-MM-JJ, dans le fuseau horaire courant de l'appareil. */
expect fun todayIso(): String

/** Horloge système en epoch ms (chronomètre E10). */
expect fun nowEpochMillis(): Long

/** Parse un instant ISO-8601 (horodatage serveur) en epoch ms, ou null si invalide. */
expect fun parseIsoInstantToEpochMillis(iso: String): Long?
