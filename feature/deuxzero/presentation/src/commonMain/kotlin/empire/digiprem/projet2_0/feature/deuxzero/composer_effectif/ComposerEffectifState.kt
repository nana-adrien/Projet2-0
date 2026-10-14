package empire.digiprem.projet2_0.feature.deuxzero.composer_effectif

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.model.MembershipStatut
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif

/** CU10 — Composer l'effectif engagé d'un camp (ajout de membres, capitaine/vice-capitaine). */
data class ComposerEffectifState(
    val camp: Camp? = null,
    val clubId: String = "",
    val effectif: List<Effectif> = emptyList(),
    val clubMembers: List<Membership> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
) {
    val membresDisponibles: List<Membership>
        get() = clubMembers.filter { membre ->
            membre.statut == MembershipStatut.ACCEPTEE && effectif.none { it.utilisateurId == membre.utilisateurId }
        }
}
