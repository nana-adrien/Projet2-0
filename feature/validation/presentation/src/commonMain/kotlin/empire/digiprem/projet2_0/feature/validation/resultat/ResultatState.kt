package empire.digiprem.projet2_0.feature.validation.resultat

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch
import empire.digiprem.projet2_0.feature.validation.model.Validation

/** E14 — Résultat à valider (CU19). */
data class ResultatState(
    val matchId: String = "",
    val deuxZeroId: String = "",
    val monCamp: Camp? = null,
    val campA: Camp? = null,
    val campB: Camp? = null,
    val resultat: ResultatMatch? = null,
    val isLoading: Boolean = true,
    val isActionLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
    val showConfirmerValidation: Boolean = false,
    val showConfirmerAcceptation: Boolean = false,
    val showRefuser: Boolean = false,
) {
    val maValidation: Validation?
        get() = monCamp?.id?.let { resultat?.validationDuCamp(it) }

    val contestationAdverse: Validation?
        get() = monCamp?.id?.let { resultat?.contestationAdverse(it) }

    /** Mon camp n'a pas encore signé ET le camp adverse a proposé une correction à traiter. */
    val enModeReponseContestation: Boolean
        get() = maValidation == null && contestationAdverse != null

    fun validationAffichee(campId: String?): Validation? = resultat?.validations?.firstOrNull { it.campId == campId }
}
