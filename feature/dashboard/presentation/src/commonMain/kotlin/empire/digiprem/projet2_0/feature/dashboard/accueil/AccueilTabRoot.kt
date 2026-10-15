package empire.digiprem.projet2_0.feature.dashboard.accueil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import empire.digiprem.projet2_0.feature.calendrier.fiche_match.FicheMatchRoot
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.matchendirect.composition.CompositionRoot
import empire.digiprem.projet2_0.feature.matchendirect.marqueur.MarqueurRoot
import empire.digiprem.projet2_0.feature.matchendirect.suivre.SuivreEnDirectRoot
import empire.digiprem.projet2_0.feature.statistiques.vote.VoteRoot
import empire.digiprem.projet2_0.feature.validation.contester.ContesterRoot
import empire.digiprem.projet2_0.feature.validation.litige.LitigeRoot
import empire.digiprem.projet2_0.feature.validation.resultat.ResultatRoot

private sealed interface AccueilTabScreen {
    data object Accueil : AccueilTabScreen
    data class FicheMatch(val match: Match, val deuxZeroId: String) : AccueilTabScreen
    data class Composition(val camp: Camp, val matchId: String, val deuxZeroId: String) : AccueilTabScreen
    data class Marqueur(val matchId: String, val deuxZeroId: String) : AccueilTabScreen
    data class SuivreEnDirect(val matchId: String, val deuxZeroId: String) : AccueilTabScreen
    data class Resultat(val camp: Camp, val matchId: String, val deuxZeroId: String) : AccueilTabScreen
    data class Contester(val matchId: String, val deuxZeroId: String) : AccueilTabScreen
    data class Litige(val matchId: String, val deuxZeroId: String) : AccueilTabScreen
    data class VoteHommeDuMatch(val matchId: String, val deuxZeroId: String) : AccueilTabScreen
}

@Composable
fun AccueilTabRoot() {
    var screen by remember { mutableStateOf<AccueilTabScreen>(AccueilTabScreen.Accueil) }

    when (val current = screen) {
        AccueilTabScreen.Accueil -> AccueilRoot(
            onMatchClick = { match, deuxZeroId -> screen = AccueilTabScreen.FicheMatch(match, deuxZeroId) },
        )

        is AccueilTabScreen.FicheMatch -> FicheMatchRoot(
            match = current.match,
            deuxZeroId = current.deuxZeroId,
            onComposerEquipe = { camp, matchId, deuxZeroId -> screen = AccueilTabScreen.Composition(camp, matchId, deuxZeroId) },
            onMarqueur = { matchId, deuxZeroId -> screen = AccueilTabScreen.Marqueur(matchId, deuxZeroId) },
            onSuivreEnDirect = { matchId, deuxZeroId -> screen = AccueilTabScreen.SuivreEnDirect(matchId, deuxZeroId) },
            onValiderResultat = { camp, matchId, deuxZeroId -> screen = AccueilTabScreen.Resultat(camp, matchId, deuxZeroId) },
            onArbitrerLitige = { matchId, deuxZeroId -> screen = AccueilTabScreen.Litige(matchId, deuxZeroId) },
            onVoterHommeDuMatch = { matchId, deuxZeroId -> screen = AccueilTabScreen.VoteHommeDuMatch(matchId, deuxZeroId) },
        )

        is AccueilTabScreen.Composition -> CompositionRoot(
            camp = current.camp,
            matchId = current.matchId,
            onDone = { screen = AccueilTabScreen.Accueil },
        )

        is AccueilTabScreen.Marqueur -> MarqueurRoot(
            matchId = current.matchId,
            deuxZeroId = current.deuxZeroId,
            onMatchTermine = { screen = AccueilTabScreen.Accueil },
        )

        is AccueilTabScreen.SuivreEnDirect -> SuivreEnDirectRoot(
            matchId = current.matchId,
            deuxZeroId = current.deuxZeroId,
        )

        is AccueilTabScreen.Resultat -> ResultatRoot(
            camp = current.camp,
            matchId = current.matchId,
            deuxZeroId = current.deuxZeroId,
            onContester = { screen = AccueilTabScreen.Contester(current.matchId, current.deuxZeroId) },
        )

        is AccueilTabScreen.Contester -> ContesterRoot(
            matchId = current.matchId,
            onEnvoye = { screen = AccueilTabScreen.Accueil },
        )

        is AccueilTabScreen.Litige -> LitigeRoot(
            matchId = current.matchId,
            deuxZeroId = current.deuxZeroId,
            onTranche = { screen = AccueilTabScreen.Accueil },
        )

        is AccueilTabScreen.VoteHommeDuMatch -> VoteRoot(
            matchId = current.matchId,
            deuxZeroId = current.deuxZeroId,
        )
    }
}
