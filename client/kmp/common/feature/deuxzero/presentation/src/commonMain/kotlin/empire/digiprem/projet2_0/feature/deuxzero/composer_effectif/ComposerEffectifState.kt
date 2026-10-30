package empire.digiprem.projet2_0.feature.deuxzero.composer_effectif

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.model.MembershipStatut
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif

/** CU10 — Composer l'effectif engagé d'un camp (ajout de membres, capitaine/vice-capitaine).
 * CU11 — Transférer un joueur vers l'autre camp du même 2-0 (RG-06, RG-09) ; le transfert vers un
 * club différent (alt de la fiche CU11) est hors scope, pas d'écran dédié dans les maquettes. */
data class ComposerEffectifState(
    val camp: Camp? = null,
    val clubId: String = "",
    val effectif: List<Effectif> = emptyList(),
    val clubMembers: List<Membership> = emptyList(),
    val autreCamp: Camp? = null,
    val isLoading: Boolean = true,
    val isTransferring: Boolean = false,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
    val showTransferDialog: Boolean = false,
    val transferTarget: Effectif? = null,
) {
    val membresDisponibles: List<Membership>
        get() = clubMembers.filter { membre ->
            membre.statut == MembershipStatut.ACCEPTEE && effectif.none { it.utilisateurId == membre.utilisateurId }
        }
}
