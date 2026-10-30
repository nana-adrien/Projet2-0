package empire.digiprem.projet2_0.feature.deuxzero.repartition

import empire.digiprem.projet2_0.feature.deuxzero.model.Camp

sealed interface RepartitionEvent {
    data class OnDone(val campA: Camp, val campB: Camp, val clubId: String) : RepartitionEvent
}
