package empire.digiprem.projet2_0.feature.deuxzero.repartition

import empire.digiprem.projet2_0.feature.deuxzero.model.Camp

sealed interface RepartitionAction {
    data class OnInit(val campA: Camp, val campB: Camp, val clubId: String) : RepartitionAction
    data object OnRetry : RepartitionAction
    data class OnAssign(val utilisateurId: String, val side: CampSide) : RepartitionAction
    data object OnTirageEquilibre : RepartitionAction
    data object OnValiderClick : RepartitionAction
}
