package empire.digiprem.projet2_0.feature.notifications.preferences

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero
import empire.digiprem.projet2_0.feature.notifications.model.NiveauNotification

/** E27 — Préférences de notification (CU26, F-NOT-02). */
data class PreferenceState(
    val deuxZeros: List<DeuxZero> = emptyList(),
    val deuxZeroSelectionneId: String = "",
    val niveau: NiveauNotification = NiveauNotification.TOUT,
    val neRienDerangerNocturne: Boolean = false,
    val isLoading: Boolean = true,
    val isActionLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
) {
    val apercu: Pair<String, String>
        get() = when (niveau) {
            NiveauNotification.TOUT, NiveauNotification.BUTS_ET_SCORE_FINAL ->
                "But ! Bleus 2 – 1 Rouges" to "Karim B. marque à la 34e."
            NiveauNotification.ME_CONCERNANT ->
                "Tu es convoqué" to "Match de dimanche, réponds avant samedi 20h."
            NiveauNotification.AUCUNE ->
                "Aucune notification" to "Ce 2-0 ne t'envoie plus rien."
        }
}
