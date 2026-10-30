package empire.digiprem.projet2_0.feature.statistiques.tableau

sealed interface TableauSaisonAction {
    data class OnInit(val deuxZeroId: String) : TableauSaisonAction
    data object OnRetry : TableauSaisonAction
    data class OnSaisonChange(val saisonId: String) : TableauSaisonAction
    data object OnOuvrirSaisonClick : TableauSaisonAction
    data object OnDismissOuvrirDialog : TableauSaisonAction
    data class OnConfirmOuvrirSaison(val libelle: String, val dateDebut: String, val dateFin: String?) : TableauSaisonAction
    data object OnCloturerSaisonClick : TableauSaisonAction
    data object OnDismissCloturerDialog : TableauSaisonAction
    data object OnConfirmCloturerSaison : TableauSaisonAction
    data object OnMessageShown : TableauSaisonAction
}
