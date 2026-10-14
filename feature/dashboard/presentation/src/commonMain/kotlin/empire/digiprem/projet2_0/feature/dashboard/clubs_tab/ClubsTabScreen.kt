package empire.digiprem.projet2_0.feature.dashboard.clubs_tab

import empire.digiprem.projet2_0.feature.clubs.model.MembershipRole
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp

/**
 * Navigation interne à l'onglet Clubs — pas de NavController : les écrans échangent des objets
 * domaine riches (Camp, etc.) directement, comme la pagination interne du Dashboard (cf.
 * architecture/navigation.md, pattern HorizontalPager piloté par le state).
 */
sealed interface ClubsTabScreen {
    data object MesClubs : ClubsTabScreen
    data object JoinClub : ClubsTabScreen
    data object CreateClub : ClubsTabScreen
    data class GestionClub(val clubId: String, val role: MembershipRole) : ClubsTabScreen
    data class CreateDeuxZero(val clubId: String) : ClubsTabScreen
    data class EngagerClub(val clubId: String) : ClubsTabScreen
    data class Repartition(val campA: Camp, val campB: Camp, val clubId: String) : ClubsTabScreen
    data class ComposerEffectif(val camp: Camp, val clubId: String) : ClubsTabScreen
}
