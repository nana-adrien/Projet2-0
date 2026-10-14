package empire.digiprem.projet2_0.core.domain.util

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSISO8601DateFormatOptions
import platform.Foundation.NSISO8601DateFormatWithFractionalSeconds
import platform.Foundation.NSISO8601DateFormatWithInternetDateTime
import platform.Foundation.NSISO8601DateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone
import platform.Foundation.timeIntervalSince1970

actual fun todayIso(): String {
    val formatter = NSDateFormatter()
    formatter.dateFormat = "yyyy-MM-dd"
    formatter.timeZone = NSTimeZone.localTimeZone
    return formatter.stringFromDate(NSDate())
}

actual fun nowEpochMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()

actual fun parseIsoInstantToEpochMillis(iso: String): Long? {
    val formatter = NSISO8601DateFormatter()
    formatter.formatOptions = NSISO8601DateFormatWithInternetDateTime or NSISO8601DateFormatWithFractionalSeconds
    val date = formatter.dateFromString(iso) ?: NSISO8601DateFormatter().dateFromString(iso)
    return date?.let { (it.timeIntervalSince1970 * 1000).toLong() }
}
