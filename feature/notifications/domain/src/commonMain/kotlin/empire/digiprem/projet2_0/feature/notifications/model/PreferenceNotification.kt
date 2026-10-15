package empire.digiprem.projet2_0.feature.notifications.model

enum class NiveauNotification { TOUT, BUTS_ET_SCORE_FINAL, ME_CONCERNANT, AUCUNE }

/** CU26 — réglage indépendant par 2-0 (F-NOT-02). La plage « Ne pas déranger » (23h-8h dans la
 * maquette) n'est pas configurable dans le modèle UML (simple Boolean), affichée en texte fixe. */
data class PreferenceNotification(
    val id: String,
    val deuxZeroId: String,
    val niveau: NiveauNotification,
    val neRienDerangerNocturne: Boolean,
)
