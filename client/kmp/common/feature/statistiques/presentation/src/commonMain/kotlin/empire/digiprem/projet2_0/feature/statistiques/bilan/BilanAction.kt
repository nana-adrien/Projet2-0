package empire.digiprem.projet2_0.feature.statistiques.bilan

sealed interface BilanAction {
    data class OnInit(val saisonId: String, val deuxZeroId: String) : BilanAction
    data object OnRetry : BilanAction
}
