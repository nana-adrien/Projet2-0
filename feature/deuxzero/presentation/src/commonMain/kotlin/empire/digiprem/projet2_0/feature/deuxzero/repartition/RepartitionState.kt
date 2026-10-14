package empire.digiprem.projet2_0.feature.deuxzero.repartition

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.model.MembershipStatut
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp

enum class CampSide { A, B }

/** E25 — Répartir les membres entre les deux camps d'un 2-0 interne (CU10). */
data class RepartitionState(
    val campA: Camp? = null,
    val campB: Camp? = null,
    val clubId: String = "",
    val clubMembers: List<Membership> = emptyList(),
    val assignments: Map<String, CampSide> = emptyMap(),
    val isLoading: Boolean = true,
    val isSubmitting: Boolean = false,
    val errorMessage: UiText? = null,
) {
    val membresAcceptes: List<Membership> get() = clubMembers.filter { it.statut == MembershipStatut.ACCEPTEE }
    val nombreCampA: Int get() = assignments.values.count { it == CampSide.A }
    val nombreCampB: Int get() = assignments.values.count { it == CampSide.B }
    val nombreNonRepartis: Int get() = membresAcceptes.size - assignments.size
    val peutValider: Boolean get() = assignments.isNotEmpty()
}
