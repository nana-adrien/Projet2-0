package empire.digiprem.projet2_0.contracts.settings

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSettingsRequest(
    val language: String?,
    val theme: String?,
    val notificationsEnabled: Boolean?,
)
