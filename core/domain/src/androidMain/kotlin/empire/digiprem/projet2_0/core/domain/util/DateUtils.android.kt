package empire.digiprem.projet2_0.core.domain.util

import java.time.LocalDate

actual fun todayIso(): String = LocalDate.now().toString()
