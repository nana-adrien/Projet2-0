package empire.digiprem.projet2_0.feature.dashboard.accueil

import empire.digiprem.projet2_0.feature.calendrier.model.Match

sealed interface AccueilEvent {
    data class OnNavigateToFicheMatch(val match: Match, val deuxZeroId: String) : AccueilEvent
    data object OnNavigateToNotifications : AccueilEvent

    /** E28 — simplifié : atterrit sur "Mes clubs" (onglet Clubs), où "Rejoindre"/"Créer" sont
     * déjà proposés en premier plan, plutôt que de dupliquer le choix de la cible ici. */
    data object OnNavigateToClubs : AccueilEvent
}
