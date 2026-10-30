package empire.digiprem.projet2_0.feature.clubs.mes_clubs

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.clubs.model.Membership

data class MesClubsState(
    val memberships: List<Membership> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
)
