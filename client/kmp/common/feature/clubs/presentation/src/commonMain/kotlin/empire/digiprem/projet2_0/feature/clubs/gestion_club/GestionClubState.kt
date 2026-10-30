package empire.digiprem.projet2_0.feature.clubs.gestion_club

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.clubs.model.Club
import empire.digiprem.projet2_0.feature.clubs.model.Membership
import empire.digiprem.projet2_0.feature.clubs.model.MembershipStatut

data class GestionClubState(
    val club: Club? = null,
    val members: List<Membership> = emptyList(),
    val currentUserId: String? = null,
    val isCurrentUserAdmin: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
) {
    val demandesEnAttente get() = members.filter { it.statut == MembershipStatut.EN_ATTENTE }
    val membresActifs get() = members.filter { it.statut == MembershipStatut.ACCEPTEE }
}
