package empire.digiprem.projet2_0.feature.notifications.dto

import kotlinx.serialization.Serializable

/** Contrat REST provisoire (Jalon 6, pas de serveur dédié encore construit). */
@Serializable
data class PreferenceNotificationDto(
    val id: String,
    val deuxZeroId: String,
    val niveau: String,
    val neRienDerangerNocturne: Boolean,
)

@Serializable
data class SetPreferenceNotificationRequest(
    val niveau: String,
    val neRienDerangerNocturne: Boolean,
)
