package empire.digiprem.projet2_0.feature.validation.resultat

sealed interface ResultatEvent {
    data object OnNavigateToContester : ResultatEvent
}
