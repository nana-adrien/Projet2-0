package empire.digiprem.projet2_0.contracts.notifications

import kotlinx.serialization.Serializable

/** CU26 — Gérer ses préférences de notification (F-NOT-02). Une préférence par couple
 * (utilisateur, 2-0). Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt). */
enum class NiveauNotification { TOUT, BUTS_ET_SCORE_FINAL, ME_CONCERNANT, AUCUNE }

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
