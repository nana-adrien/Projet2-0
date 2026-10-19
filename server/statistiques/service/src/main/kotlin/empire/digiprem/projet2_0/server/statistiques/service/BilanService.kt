package empire.digiprem.projet2_0.server.statistiques.service

import empire.digiprem.projet2_0.contracts.statistiques.BilanDto
import empire.digiprem.projet2_0.contracts.statistiques.ClassementEntryDto
import empire.digiprem.projet2_0.contracts.statistiques.PalmaresEntryDto
import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.calendrier.infra.repository.CompositionRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.EvenementRepository
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.deuxzero.infra.entity.SaisonEntity
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.SaisonRepository
import empire.digiprem.projet2_0.server.deuxzero.service.SaisonService
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * CU25 — bilan de clôture de saison (RG-17 : camp champion) et palmarès d'un 2-0.
 *
 * RG-17 : parmi les matchs OFFICIEL de la saison, on compare les 2 camps dans l'ordre :
 * 1) le plus de victoires ; égalité -> 2) différence de buts cumulée ; égalité -> 3) buts
 * marqués ; égalité -> 4) résultat du dernier match officiel (date puis heure). Si l'égalité
 * persiste même au dernier critère, [BilanDto.campChampionId] reste nul et
 * [BilanDto.egaliteTotale] vaut true — on ne force jamais un gagnant arbitraire. Une saison
 * sans aucun match officiel est traitée de la même façon (impossible de départager).
 */
@Service
class BilanService(
    private val saisonRepository: SaisonRepository,
    private val saisonService: SaisonService,
    private val evenementRepository: EvenementRepository,
    private val compositionRepository: CompositionRepository,
    private val userRepository: UserRepository,
    private val statistiquesService: StatistiquesService,
) {

    @Transactional
    fun cloturerEtBilan(saisonId: UUID, requesterId: UUID): BilanDto {
        // SaisonService.closeSaison vérifie l'organisateur et lève ForbiddenActionException si
        // la saison est déjà clôturée (RG-18, saison figée) — on ne duplique pas ce contrôle ici.
        saisonService.closeSaison(saisonId, requesterId)
        val saison = findSaison(saisonId)
        val bilan = calculerBilan(saison)

        if (bilan.campChampionId != saison.campChampionId?.toString()) {
            saisonRepository.save(
                SaisonEntity(
                    id = saison.id, deuxZeroId = saison.deuxZeroId, libelle = saison.libelle,
                    dateDebut = saison.dateDebut, dateFin = saison.dateFin, statut = saison.statut,
                    campChampionId = bilan.campChampionId?.let(UUID::fromString),
                ),
            )
        }
        return bilan
    }

    /** Lecture idempotente : recalcule le bilan à partir des données (immuables une fois la
     * saison clôturée et les matchs OFFICIEL) — ne modifie rien en base. */
    fun bilan(saisonId: UUID): BilanDto {
        val saison = findSaison(saisonId)
        if (saison.statut != STATUT_CLOTUREE) {
            throw ForbiddenActionException("Cette saison n'est pas encore clôturée")
        }
        return calculerBilan(saison)
    }

    fun palmares(deuxZeroId: UUID): List<PalmaresEntryDto> =
        saisonRepository.findAllByDeuxZeroId(deuxZeroId)
            .filter { it.statut == STATUT_CLOTUREE }
            .sortedByDescending { it.dateDebut }
            .map { PalmaresEntryDto(saisonId = it.id.toString(), libelle = it.libelle, campChampionId = it.campChampionId?.toString()) }

    private fun calculerBilan(saison: SaisonEntity): BilanDto {
        val saisonId = saison.id!!
        val (campAId, campBId) = statistiquesService.deuxCampsDe(saison.deuxZeroId)
        val matchs = statistiquesService.matchsOfficielsDeuxZero(saisonId).sortedWith(compareBy({ it.date }, { it.heure }))

        var victoiresA = 0
        var victoiresB = 0
        var butsA = 0
        var butsB = 0
        matchs.forEach { match ->
            val scoreA = match.scoreCampA ?: 0
            val scoreB = match.scoreCampB ?: 0
            butsA += scoreA
            butsB += scoreB
            when {
                scoreA > scoreB -> victoiresA++
                scoreB > scoreA -> victoiresB++
            }
        }

        val (campChampionId, egaliteTotale) = determinerChampion(matchs, campAId, campBId, victoiresA, victoiresB, butsA, butsB)

        val evenements = matchs.flatMap { evenementRepository.findAllByMatchIdOrderByPeriodeAscMinuteAsc(it.id!!) }
        val compositions = matchs.flatMap { compositionRepository.findAllByMatchId(it.id!!) }
        val meilleurButeur = StatistiquesAggregator.butsParJoueur(evenements).meilleurOuNull()
        val meilleurPasseur = StatistiquesAggregator.passesParJoueur(evenements).meilleurOuNull()
        val plusAssidu = StatistiquesAggregator.presencesParJoueur(compositions).meilleurOuNull()

        return BilanDto(
            saisonId = saisonId.toString(),
            campChampionId = campChampionId?.toString(),
            egaliteTotale = egaliteTotale,
            scoreCampA = butsA,
            scoreCampB = butsB,
            meilleurButeur = meilleurButeur,
            meilleurPasseur = meilleurPasseur,
            plusAssidu = plusAssidu,
            plusBeauMatchId = plusBeauMatch(matchs)?.id?.toString(),
            plusLongueSerie = plusLongueSerieHistorique(matchs, campAId, campBId),
        )
    }

    private fun determinerChampion(
        matchs: List<MatchEntity>,
        campAId: UUID,
        campBId: UUID,
        victoiresA: Int,
        victoiresB: Int,
        butsA: Int,
        butsB: Int,
    ): Pair<UUID?, Boolean> {
        if (matchs.isEmpty()) return null to true

        if (victoiresA != victoiresB) return (if (victoiresA > victoiresB) campAId else campBId) to false

        val diffA = butsA - butsB
        val diffB = butsB - butsA
        if (diffA != diffB) return (if (diffA > diffB) campAId else campBId) to false

        if (butsA != butsB) return (if (butsA > butsB) campAId else campBId) to false

        val dernierMatch = matchs.last()
        val vainqueurDernierMatch = statistiquesService.vainqueurDuMatch(dernierMatch, campAId, campBId)
        return if (vainqueurDernierMatch != null) vainqueurDernierMatch to false else null to true
    }

    /** Aucune note de "beauté" n'existe dans le modèle (pas de vote de spectateurs sur les
     * matchs) — proxy documenté : le match avec le plus de buts cumulés, le plus ancien en cas
     * d'égalité. */
    private fun plusBeauMatch(matchs: List<MatchEntity>): MatchEntity? =
        matchs.maxWithOrNull(compareBy({ (it.scoreCampA ?: 0) + (it.scoreCampB ?: 0) }, { it.date }))

    /** Plus longue série de victoires consécutives (d'un camp ou de l'autre) observée pendant
     * toute la saison — distinct de la série "en cours" du tableau de saison (CU22). */
    private fun plusLongueSerieHistorique(matchsTriesParDateAsc: List<MatchEntity>, campAId: UUID, campBId: UUID): Int {
        var maxLongueur = 0
        var campCourant: UUID? = null
        var longueurCourante = 0
        matchsTriesParDateAsc.forEach { match ->
            val vainqueur = statistiquesService.vainqueurDuMatch(match, campAId, campBId)
            if (vainqueur == null) {
                campCourant = null
                longueurCourante = 0
                return@forEach
            }
            longueurCourante = if (vainqueur == campCourant) longueurCourante + 1 else 1
            campCourant = vainqueur
            if (longueurCourante > maxLongueur) maxLongueur = longueurCourante
        }
        return maxLongueur
    }

    private fun findSaison(saisonId: UUID): SaisonEntity =
        saisonRepository.findById(saisonId).orElseThrow { ResourceNotFoundException("Saison introuvable") }

    private fun Map<UUID, Int>.meilleurOuNull(): ClassementEntryDto? =
        entries.maxByOrNull { it.value }?.let { (userId, valeur) -> ClassementEntryDto(userId.toString(), pseudoDe(userId), valeur) }

    private fun pseudoDe(userId: UUID): String = userRepository.findById(userId).map { it.pseudo }.orElse("")
}
