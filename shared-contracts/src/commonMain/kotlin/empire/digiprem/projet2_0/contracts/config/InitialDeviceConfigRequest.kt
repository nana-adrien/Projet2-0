package empire.digiprem.projet2_0.contracts.config

data class InitialDeviceConfigRequest(
    val deviceId: String,
    val pushToken: String,
    val platform: String,
    val appVersion: String,
    val timezone: String
)