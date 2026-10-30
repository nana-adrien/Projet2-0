package empire.digiprem.projet2_0.feature.dashboard.clubs_tab

import empire.digiprem.projet2_0.feature.calendrier.model.Match
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
    data class CreerAmical(val clubId: String) : ClubsTabScreen
    data class Repartition(val campA: Camp, val campB: Camp, val clubId: String) : ClubsTabScreen
    data class ComposerEffectif(val camp: Camp, val clubId: String) : ClubsTabScreen
    data class Calendrier(val deuxZeroId: String) : ClubsTabScreen
    data class FicheMatch(val match: Match, val deuxZeroId: String) : ClubsTabScreen
    data class Composition(val camp: Camp, val matchId: String, val deuxZeroId: String) : ClubsTabScreen
    data class Marqueur(val matchId: String, val deuxZeroId: String) : ClubsTabScreen
    data class SuivreEnDirect(val matchId: String, val deuxZeroId: String) : ClubsTabScreen
    data class Resultat(val camp: Camp, val matchId: String, val deuxZeroId: String) : ClubsTabScreen
    data class Contester(val matchId: String, val deuxZeroId: String) : ClubsTabScreen
    data class Litige(val matchId: String, val deuxZeroId: String) : ClubsTabScreen
    data class VoteHommeDuMatch(val matchId: String, val deuxZeroId: String) : ClubsTabScreen
    data class Parametres(val deuxZeroId: String) : ClubsTabScreen
}
