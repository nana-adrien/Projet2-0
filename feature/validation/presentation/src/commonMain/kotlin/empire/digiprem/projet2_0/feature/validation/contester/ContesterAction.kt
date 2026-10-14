package empire.digiprem.projet2_0.feature.validation.contester

sealed interface ContesterAction {
    data class OnInit(val matchId: String) : ContesterAction
    data class OnTypeErreurChange(val typeErreur: TypeErreur) : ContesterAction
    data class OnEvenementChange(val evenementId: String) : ContesterAction
    data class OnBonJoueurChange(val value: String) : ContesterAction
    data class OnCommentaireChange(val value: String) : ContesterAction
    data object OnEnvoyerClick : ContesterAction
}
