package empire.digiprem.projet2_0.feature.calendrier.calendrier

import empire.digiprem.projet2_0.feature.calendrier.model.Match

sealed interface CalendrierEvent {
    data class OnNavigateToFicheMatch(val match: Match) : CalendrierEvent
}
