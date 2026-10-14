package empire.digiprem.projet2_0.feature.deuxzero.composer_effectif

sealed interface ComposerEffectifEvent {
    data object OnDone : ComposerEffectifEvent
    data class OnViewCalendrier(val deuxZeroId: String) : ComposerEffectifEvent
}
