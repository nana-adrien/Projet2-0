package empire.digiprem.projet2_0.contracts.config

import kotlinx.serialization.Serializable

@Serializable
class UpdatePushTokenRequest(
    val pushToken: String,
    val deviceId: String
) {
}