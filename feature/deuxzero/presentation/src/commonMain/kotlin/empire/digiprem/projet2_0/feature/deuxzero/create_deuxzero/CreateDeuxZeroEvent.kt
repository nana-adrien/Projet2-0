package empire.digiprem.projet2_0.feature.deuxzero.create_deuxzero

import empire.digiprem.projet2_0.feature.deuxzero.model.Camp

sealed interface CreateDeuxZeroEvent {
    data class OnInterneCreated(val deuxZeroId: String, val clubId: String, val campA: Camp, val campB: Camp) : CreateDeuxZeroEvent
    data class OnManageEffectif(val camp: Camp, val clubId: String) : CreateDeuxZeroEvent
    data object OnFinished : CreateDeuxZeroEvent
}
