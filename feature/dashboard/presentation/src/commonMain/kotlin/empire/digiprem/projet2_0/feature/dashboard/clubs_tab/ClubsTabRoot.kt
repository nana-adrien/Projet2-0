package empire.digiprem.projet2_0.feature.dashboard.clubs_tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import empire.digiprem.projet2_0.feature.calendrier.calendrier.CalendrierRoot
import empire.digiprem.projet2_0.feature.calendrier.fiche_match.FicheMatchRoot
import empire.digiprem.projet2_0.feature.clubs.create_club.CreateClubRoot
import empire.digiprem.projet2_0.feature.clubs.gestion_club.GestionClubRoot
import empire.digiprem.projet2_0.feature.clubs.join_club.JoinClubRoot
import empire.digiprem.projet2_0.feature.clubs.mes_clubs.MesClubsRoot
import empire.digiprem.projet2_0.feature.deuxzero.composer_effectif.ComposerEffectifRoot
import empire.digiprem.projet2_0.feature.deuxzero.create_deuxzero.CreateDeuxZeroRoot
import empire.digiprem.projet2_0.feature.deuxzero.engager_club.EngagerClubRoot
import empire.digiprem.projet2_0.feature.deuxzero.repartition.RepartitionRoot

@Composable
fun ClubsTabRoot() {
    val backStack = remember { mutableStateListOf<ClubsTabScreen>(ClubsTabScreen.MesClubs) }

    fun push(screen: ClubsTabScreen) {
        backStack.add(screen)
    }

    fun pop() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }

    fun popToMesClubs() {
        while (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }

    fun popToGestionClub() {
        while (backStack.size > 1 && backStack.last() !is ClubsTabScreen.GestionClub) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    when (val current = backStack.last()) {
        ClubsTabScreen.MesClubs -> MesClubsRoot(
            onNavigateToGestion = { clubId, role -> push(ClubsTabScreen.GestionClub(clubId, role)) },
            onNavigateToJoin = { push(ClubsTabScreen.JoinClub) },
            onNavigateToCreate = { push(ClubsTabScreen.CreateClub) },
        )

        ClubsTabScreen.JoinClub -> JoinClubRoot(onJoined = { pop() })

        ClubsTabScreen.CreateClub -> CreateClubRoot(onCreated = { pop() })

        is ClubsTabScreen.GestionClub -> GestionClubRoot(
            clubId = current.clubId,
            initialRole = current.role,
            onLeftClub = { popToMesClubs() },
            onNavigateToCreateDeuxZero = { clubId -> push(ClubsTabScreen.CreateDeuxZero(clubId)) },
            onNavigateToEngagerClub = { clubId -> push(ClubsTabScreen.EngagerClub(clubId)) },
        )

        is ClubsTabScreen.CreateDeuxZero -> CreateDeuxZeroRoot(
            clubId = current.clubId,
            onInterneCreated = { _, clubId, campA, campB -> push(ClubsTabScreen.Repartition(campA, campB, clubId)) },
            onManageEffectif = { camp, clubId -> push(ClubsTabScreen.ComposerEffectif(camp, clubId)) },
            onFinished = { pop() },
        )

        is ClubsTabScreen.EngagerClub -> EngagerClubRoot(
            onEngaged = { camp -> push(ClubsTabScreen.ComposerEffectif(camp, current.clubId)) },
        )

        is ClubsTabScreen.Repartition -> RepartitionRoot(
            campA = current.campA,
            campB = current.campB,
            clubId = current.clubId,
            onDone = { campA, _, clubId -> push(ClubsTabScreen.ComposerEffectif(campA, clubId)) },
        )

        is ClubsTabScreen.ComposerEffectif -> ComposerEffectifRoot(
            camp = current.camp,
            clubId = current.clubId,
            onDone = { popToGestionClub() },
            onViewCalendrier = { deuxZeroId -> push(ClubsTabScreen.Calendrier(deuxZeroId)) },
        )

        is ClubsTabScreen.Calendrier -> CalendrierRoot(
            deuxZeroId = current.deuxZeroId,
            onMatchClick = { match -> push(ClubsTabScreen.FicheMatch(match)) },
        )

        is ClubsTabScreen.FicheMatch -> FicheMatchRoot(match = current.match)
    }
}
