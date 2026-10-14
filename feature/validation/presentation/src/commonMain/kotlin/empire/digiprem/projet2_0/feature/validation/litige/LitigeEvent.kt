package empire.digiprem.projet2_0.feature.validation.litige

sealed interface LitigeEvent {
    data object OnTranche : LitigeEvent
}
