package empire.digiprem.projet2_0.feature.deuxzero.engager_club

import empire.digiprem.projet2_0.feature.deuxzero.model.Camp

sealed interface EngagerClubEvent {
    data class OnEngaged(val camp: Camp) : EngagerClubEvent
}
