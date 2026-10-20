package empire.digiprem.projet2_0.server.statistiques.service

import empire.digiprem.projet2_0.contracts.statistiques.ClassementEntryDto
import empire.digiprem.projet2_0.contracts.statistiques.ClassementsDto
import empire.digiprem.projet2_0.contracts.statistiques.ConfrontationMatchDto
import empire.digiprem.projet2_0.contracts.statistiques.ConfrontationsDto
import empire.digiprem.projet2_0.contracts.statistiques.PointMensuelDto
import empire.digiprem.projet2_0.contracts.statistiques.StatistiquesClubDto
import empire.digiprem.projet2_0.contracts.statistiques.TableauSaisonDto
import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.calendrier.infra.repository.CompositionRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.EvenementRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.MatchRepository
import empire.digiprem.projet2_0.server.clubs.infra.repository.ClubRepository
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.CampRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.SaisonRepository
import empire.digiprem.projet2_0.server.statistiques.infra.repository.VoteRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

/**
 * CU22 (F-STA-01 à F-STA-07) — tableau de saison, classements, statistiques de club et
 * confrontations. RG : seuls les matchs [TYPE_MATCH_DEUX_ZERO] à [STATUT_OFFICIEL] sont
 * comptabilisés — un match encore EN_ATTENTE_DE_VALIDATION ou EN_LITIGE n'est pas encore
 * fiable (score susceptible de changer), et le module amical CU21 (`server/amical`) est
 * volontairement exclu de ce jalon (limitation documentée, pas une dépendance sur un module en
 * cours d'intégration parallèle). "Meilleur gardien" n'est pas implémenté : aucune donnée
 * d'arrêt/clean-sheet n'existe dans le modèle Evenement actuel.
 *
 * Toutes les méthodes publiques (et les helpers internes partagés avec [BilanService]) sont
 * désormais @Transactional : elles naviguent les relations LAZY MatchEntity.saison, CampEntity.
 * deuxZero/club, etc.
 */
@Service
class StatistiquesService(
    private val matchRepository: MatchRepository,
    private val evenementRepository: EvenementRepository,
    private val compositionRepository: CompositionRepository,
    private val saisonRepository: SaisonRepository,
    private val campRepository: CampRepository,
    private val clubRepository: ClubRepository,
    private val userRepository: UserRepository,
    private val voteRepository: VoteRepository,
) {

    @Transactional
    fun getTableauSaison(saisonId: UUID): TableauSaisonDto {
        val saison = saisonRepository.findById(saisonId).orElseThrow { ResourceNotFoundException("Saison introuvable") }
        val (campAId, campBId) = deuxCampsDe(saison.deuxZero.id!!)
        val matchs = matchsOfficielsDeuxZero(saisonId).sortedWith(compareBy({ it.date }, { it.heure }))

        var victoiresCampA = 0
        var victoiresCampB = 0
        var nuls = 0
        var butsCampA = 0
        var butsCampB = 0
        matchs.forEach { match ->
            val scoreA = match.scoreCampA ?: 0
            val scoreB = match.scoreCampB ?: 0
            butsCampA += scoreA
            butsCampB += scoreB
            when {
                scoreA > scoreB -> victoiresCampA++
                scoreB > scoreA -> victoiresCampB++
                else -> nuls++
            }
        }

        val (serieCampId, longueurSerie) = serieEnCours(matchs, campAId, campBId)

        val evolutionMensuelle = matchs
            .groupBy { "%04d-%02d".format(it.date.year, it.date.monthValue) }
            .toSortedMap()
            .map { (mois, matchsDuMois) ->
                PointMensuelDto(
                    mois = mois,
                    butsCampA = matchsDuMois.sumOf { it.scoreCampA ?: 0 },
                    butsCampB = matchsDuMois.sumOf { it.scoreCampB ?: 0 },
                )
            }

        return TableauSaisonDto(
            victoiresCampA = victoiresCampA,
            victoiresCampB = victoiresCampB,
            nuls = nuls,
            butsCampA = butsCampA,
            butsCampB = butsCampB,
            serieEnCoursCampId = serieCampId?.toString(),
            longueurSerieEnCours = longueurSerie,
            evolutionMensuelle = evolutionMensuelle,
        )
    }

    @Transactional
    fun getClassements(saisonId: UUID): ClassementsDto {
        saisonRepository.findById(saisonId).orElseThrow { ResourceNotFoundException("Saison introuvable") }
        val matchs = matchsOfficielsDeuxZero(saisonId)
        val evenements = matchs.flatMap { evenementRepository.findAllByMatch_IdOrderByPeriodeAscMinuteAsc(it.id!!) }
        val compositions = matchs.flatMap { compositionRepository.findAllByMatch_Id(it.id!!) }

        val buts = StatistiquesAggregator.butsParJoueur(evenements)
        val passes = StatistiquesAggregator.passesParJoueur(evenements)
        val presences = StatistiquesAggregator.presencesParJoueur(compositions)
        val butsPasseurs = StatistiquesAggregator.combine(buts, passes)

        val hommesDuMatch = matchs
            .flatMap { calculerGagnantsVote(voteRepository.findAllByMatch_Id(it.id!!)) }
            .groupingBy { it }
            .eachCount()

        val totalButs = buts.values.sum()
        val moyenneButsParMatch = if (matchs.isEmpty()) 0.0 else totalButs.toDouble() / matchs.size

        return ClassementsDto(
            meilleursButeurs = buts.toClassementDesc(),
            meilleursPasseurs = passes.toClassementDesc(),
            meilleursButeursPasseurs = butsPasseurs.toClassementDesc(),
            plusAssidus = presences.toClassementDesc(),
            hommesDuMatch = hommesDuMatch.toClassementDesc(),
            moyenneButsParMatch = moyenneButsParMatch,
        )
    }

    @Transactional
    fun getStatistiquesClub(clubId: UUID, dateDebut: String?, dateFin: String?): StatistiquesClubDto {
        clubRepository.findById(clubId).orElseThrow { ResourceNotFoundException("Club introuvable") }
        val debut = dateDebut?.let(LocalDate::parse)
        val fin = dateFin?.let(LocalDate::parse)

        val campsDuClub = campRepository.findAll().filter { it.club?.id == clubId }

        var nombreMatchs = 0
        var victoires = 0
        var nuls = 0
        var defaites = 0
        var butsMarques = 0
        var butsEncaisses = 0

        campsDuClub.forEach { camp ->
            val (campAId, campBId) = deuxCampsDe(camp.deuxZero.id!!)
            val estCampA = camp.id == campAId
            matchsOfficielsDuDeuxZero(camp.deuxZero.id!!, debut, fin).forEach { match ->
                val scoreFor = if (estCampA) match.scoreCampA ?: 0 else match.scoreCampB ?: 0
                val scoreAgainst = if (estCampA) match.scoreCampB ?: 0 else match.scoreCampA ?: 0
                nombreMatchs++
                butsMarques += scoreFor
                butsEncaisses += scoreAgainst
                when {
                    scoreFor > scoreAgainst -> victoires++
                    scoreFor < scoreAgainst -> defaites++
                    else -> nuls++
                }
            }
        }

        return StatistiquesClubDto(
            clubId = clubId.toString(),
            nombreMatchsDeuxZero = nombreMatchs,
            victoires = victoires,
            nuls = nuls,
            defaites = defaites,
            butsMarques = butsMarques,
            butsEncaisses = butsEncaisses,
        )
    }

    @Transactional
    fun getConfrontations(clubAId: UUID, clubBId: UUID, dateDebut: String?, dateFin: String?): ConfrontationsDto {
        clubRepository.findById(clubAId).orElseThrow { ResourceNotFoundException("Club A introuvable") }
        clubRepository.findById(clubBId).orElseThrow { ResourceNotFoundException("Club B introuvable") }
        val debut = dateDebut?.let(LocalDate::parse)
        val fin = dateFin?.let(LocalDate::parse)

        val campsParDeuxZero = campRepository.findAll().groupBy { it.deuxZero.id }
        val confrontations = campsParDeuxZero.filter { (_, camps) ->
            camps.size == 2 && setOf(camps[0].club?.id, camps[1].club?.id) == setOf(clubAId, clubBId)
        }

        val matchsDto = confrontations
            .flatMap { (deuxZeroId, camps) ->
                // "campA" au sens de MatchEntity.scoreCampA correspond à camps[0] (même convention
                // que deuxCampsDe/EvenementService.deuxCampsDe).
                val clubIdDuCampA = camps[0].club?.id
                val clubIdDuCampB = camps[1].club?.id
                matchsOfficielsDuDeuxZero(deuxZeroId!!, debut, fin).map { match ->
                    val scoreA = match.scoreCampA ?: 0
                    val scoreB = match.scoreCampB ?: 0
                    val vainqueurClubId = when {
                        scoreA == scoreB -> null
                        scoreA > scoreB -> clubIdDuCampA
                        else -> clubIdDuCampB
                    }
                    ConfrontationMatchDto(
                        matchId = match.id.toString(),
                        date = match.date.toString(),
                        scoreCampA = scoreA,
                        scoreCampB = scoreB,
                        clubVainqueurId = vainqueurClubId?.toString(),
                    )
                }
            }
            .sortedBy { it.date }

        return ConfrontationsDto(clubAId = clubAId.toString(), clubBId = clubBId.toString(), matchs = matchsDto)
    }

    // --- Helpers internes partagés avec BilanService ---

    @Transactional
    internal fun matchsOfficielsDeuxZero(saisonId: UUID): List<MatchEntity> =
        matchRepository.findAllBySaison_IdAndStatut(saisonId, STATUT_OFFICIEL).filter { it.type == TYPE_MATCH_DEUX_ZERO }

    @Transactional
    internal fun matchsOfficielsDuDeuxZero(deuxZeroId: UUID, debut: LocalDate?, fin: LocalDate?): List<MatchEntity> =
        saisonRepository.findAllByDeuxZero_Id(deuxZeroId)
            .flatMap { matchsOfficielsDeuxZero(it.id!!) }
            .filter { (debut == null || !it.date.isBefore(debut)) && (fin == null || !it.date.isAfter(fin)) }

    /** RG-04 : toujours exactement 2 camps par 2-0 ; l'ordre (A/B) suit l'ordre de retour du
     * repository (même convention que EvenementService.deuxCampsDe, dupliquée ici car le module
     * calendrier ne peut pas être modifié pour exposer cette logique). */
    @Transactional
    internal fun deuxCampsDe(deuxZeroId: UUID): Pair<UUID, UUID> {
        val camps = campRepository.findAllByDeuxZero_Id(deuxZeroId)
        if (camps.size != 2) throw ResourceNotFoundException("Ce 2-0 n'a pas exactement 2 camps")
        return camps[0].id!! to camps[1].id!!
    }

    /** Série de victoires consécutives en cours, en remontant depuis le match le plus récent. */
    internal fun serieEnCours(matchsTriesParDateAsc: List<MatchEntity>, campAId: UUID, campBId: UUID): Pair<UUID?, Int> {
        var serieCampId: UUID? = null
        var longueur = 0
        for (match in matchsTriesParDateAsc.asReversed()) {
            val vainqueur = vainqueurDuMatch(match, campAId, campBId) ?: break
            if (serieCampId == null) {
                serieCampId = vainqueur
                longueur = 1
            } else if (vainqueur == serieCampId) {
                longueur++
            } else {
                break
            }
        }
        return serieCampId to longueur
    }

    internal fun vainqueurDuMatch(match: MatchEntity, campAId: UUID, campBId: UUID): UUID? {
        val scoreA = match.scoreCampA ?: 0
        val scoreB = match.scoreCampB ?: 0
        return when {
            scoreA > scoreB -> campAId
            scoreB > scoreA -> campBId
            else -> null
        }
    }

    private fun Map<UUID, Int>.toClassementDesc(): List<ClassementEntryDto> =
        entries
            .sortedByDescending { it.value }
            .map { (userId, valeur) -> ClassementEntryDto(userId.toString(), pseudoDe(userId), valeur) }

    private fun pseudoDe(userId: UUID): String = userRepository.findById(userId).map { it.pseudo }.orElse("")
}
