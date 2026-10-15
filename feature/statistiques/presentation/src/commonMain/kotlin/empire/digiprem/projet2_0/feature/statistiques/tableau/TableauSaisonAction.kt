package empire.digiprem.projet2_0.feature.statistiques.tableau

sealed interface TableauSaisonAction {
    data class OnInit(val deuxZeroId: String) : TableauSaisonAction
    data object OnRetry : TableauSaisonAction
    data class OnSaisonChange(val saisonId: String) : TableauSaisonAction
}
