package empire.digiprem.projet2_0.core.domain.util

import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeParseException

actual fun todayIso(): String = LocalDate.now().toString()

actual fun nowEpochMillis(): Long = System.currentTimeMillis()

actual fun parseIsoInstantToEpochMillis(iso: String): Long? =
    try {
        Instant.parse(iso).toEpochMilli()
    } catch (e: DateTimeParseException) {
        null
    }
