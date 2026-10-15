package empire.digiprem.projet2_0.feature.dashboard.clubs_tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import empire.digiprem.projet2_0.feature.amical.creer.CreerAmicalRoot
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
import empire.digiprem.projet2_0.feature.matchendirect.composition.CompositionRoot
import empire.digiprem.projet2_0.feature.matchendirect.marqueur.MarqueurRoot
import empire.digiprem.projet2_0.feature.matchendirect.suivre.SuivreEnDirectRoot
import empire.digiprem.projet2_0.feature.statistiques.vote.VoteRoot
import empire.digiprem.projet2_0.feature.validation.contester.ContesterRoot
import empire.digiprem.projet2_0.feature.validation.litige.LitigeRoot
import empire.digiprem.projet2_0.feature.validation.resultat.ResultatRoot

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
            onNavigateToOrganiserAmical = { clubId -> push(ClubsTabScreen.CreerAmical(clubId)) },
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
            onMatchClick = { match -> push(ClubsTabScreen.FicheMatch(match, current.deuxZeroId)) },
        )

        is ClubsTabScreen.FicheMatch -> FicheMatchRoot(
            match = current.match,
            deuxZeroId = current.deuxZeroId,
            onComposerEquipe = { camp, matchId, deuxZeroId -> push(ClubsTabScreen.Composition(camp, matchId, deuxZeroId)) },
            onMarqueur = { matchId, deuxZeroId -> push(ClubsTabScreen.Marqueur(matchId, deuxZeroId)) },
            onSuivreEnDirect = { matchId, deuxZeroId -> push(ClubsTabScreen.SuivreEnDirect(matchId, deuxZeroId)) },
            onValiderResultat = { camp, matchId, deuxZeroId -> push(ClubsTabScreen.Resultat(camp, matchId, deuxZeroId)) },
            onArbitrerLitige = { matchId, deuxZeroId -> push(ClubsTabScreen.Litige(matchId, deuxZeroId)) },
            onVoterHommeDuMatch = { matchId, deuxZeroId -> push(ClubsTabScreen.VoteHommeDuMatch(matchId, deuxZeroId)) },
        )

        is ClubsTabScreen.Composition -> CompositionRoot(
            camp = current.camp,
            matchId = current.matchId,
            onDone = { pop() },
        )

        is ClubsTabScreen.Marqueur -> MarqueurRoot(
            matchId = current.matchId,
            deuxZeroId = current.deuxZeroId,
            onMatchTermine = { pop() },
        )

        is ClubsTabScreen.SuivreEnDirect -> SuivreEnDirectRoot(
            matchId = current.matchId,
            deuxZeroId = current.deuxZeroId,
        )

        is ClubsTabScreen.Resultat -> ResultatRoot(
            camp = current.camp,
            matchId = current.matchId,
            deuxZeroId = current.deuxZeroId,
            onContester = { push(ClubsTabScreen.Contester(current.matchId, current.deuxZeroId)) },
        )

        is ClubsTabScreen.Contester -> ContesterRoot(
            matchId = current.matchId,
            onEnvoye = { pop() },
        )

        is ClubsTabScreen.Litige -> LitigeRoot(
            matchId = current.matchId,
            deuxZeroId = current.deuxZeroId,
            onTranche = { pop() },
        )

        is ClubsTabScreen.CreerAmical -> CreerAmicalRoot(
            clubId = current.clubId,
            onNavigateBack = { pop() },
            onCree = { popToGestionClub() },
        )

        is ClubsTabScreen.VoteHommeDuMatch -> VoteRoot(
            matchId = current.matchId,
            deuxZeroId = current.deuxZeroId,
        )
    }
}
