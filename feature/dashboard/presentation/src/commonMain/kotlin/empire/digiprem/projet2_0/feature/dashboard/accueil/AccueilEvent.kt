package empire.digiprem.projet2_0.feature.dashboard.accueil

import empire.digiprem.projet2_0.feature.calendrier.model.Match

sealed interface AccueilEvent {
    data class OnNavigateToFicheMatch(val match: Match) : AccueilEvent
}
