package empire.digiprem.projet2_0.contracts.config

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class ConnectedDeviceResponse(
    val deviceId: String,
    val platform: String,
    val appVersion: String,
    val timezone: String,
    val lastUsedAt: Instant
)
