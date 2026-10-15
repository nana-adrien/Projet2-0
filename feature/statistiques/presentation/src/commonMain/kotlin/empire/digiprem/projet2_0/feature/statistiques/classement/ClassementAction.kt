package empire.digiprem.projet2_0.feature.statistiques.classement

import empire.digiprem.projet2_0.feature.statistiques.model.TypeClassement

sealed interface ClassementAction {
    data class OnInit(val deuxZeroId: String) : ClassementAction
    data object OnRetry : ClassementAction
    data class OnTypeChange(val type: TypeClassement) : ClassementAction
}
