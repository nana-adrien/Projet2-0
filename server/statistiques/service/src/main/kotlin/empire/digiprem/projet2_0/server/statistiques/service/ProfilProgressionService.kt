package empire.digiprem.projet2_0.server.statistiques.service

import empire.digiprem.projet2_0.contracts.statistiques.PointProgressionDto
import empire.digiprem.projet2_0.contracts.statistiques.ProfilPublicDto
import empire.digiprem.projet2_0.contracts.statistiques.ProgressionMensuelleDto
import empire.digiprem.projet2_0.contracts.statistiques.StatistiquesUtilisateurDto
import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.calendrier.infra.repository.CompositionRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.EvenementRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.MatchRepository
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.CampRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.SaisonRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

private const val GRANULARITE_MOIS = "MOIS"
private const val GRANULARITE_SAISON = "SAISON"

/**
 * CU23 — profil public (F-PRO-04/06, masquage de confidentialité) et progression (courbe
 * mensuelle ou par saison). Les stats sont toujours limitées aux matchs DEUX_ZERO à
 * [STATUT_OFFICIEL] (mêmes règles que StatistiquesService/CU22).
 */
@Service
class ProfilProgressionService(
    private val userRepository: UserRepository,
    private val matchRepository: MatchRepository,
    private val evenementRepository: EvenementRepository,
    private val compositionRepository: CompositionRepository,
    private val saisonRepository: SaisonRepository,
    private val campRepository: CampRepository,
) {

    @Transactional
    fun getProfilPublic(userId: UUID, viewerId: UUID, deuxZeroId: UUID?, clubId: UUID?): ProfilPublicDto {
        val user = userRepository.findById(userId).orElseThrow { ResourceNotFoundException("Utilisateur introuvable") }
        val matchs = matchsConcernes(userId, deuxZeroId, clubId)
        val stats = statistiquesDe(userId, matchs)

        val estSoiMeme = viewerId == userId
        return ProfilPublicDto(
            utilisateurId = userId.toString(),
            pseudo = user.pseudo,
            nom = if (!estSoiMeme && user.cacherNomReel) null else user.nom,
            prenom = if (!estSoiMeme && user.cacherNomReel) null else user.prenom,
            photo = user.photo,
            dateNaissance = if (!estSoiMeme && user.cacherAge) null else user.dateNaissance?.toString(),
            statistiques = stats,
        )
    }

    @Transactional
    fun getProgression(userId: UUID, deuxZeroId: UUID?, granularite: String): ProgressionMensuelleDto {
        userRepository.findById(userId).orElseThrow { ResourceNotFoundException("Utilisateur introuvable") }
        val matchs = matchsConcernes(userId, deuxZeroId, clubId = null)

        val points = when (granularite.uppercase()) {
            GRANULARITE_SAISON -> progressionParSaison(userId, matchs)
            else -> progressionParMois(userId, matchs)
        }
        return ProgressionMensuelleDto(points = points)
    }

    private fun progressionParMois(userId: UUID, matchs: List<MatchEntity>): List<PointProgressionDto> =
        matchs
            .groupBy { "%04d-%02d".format(it.date.year, it.date.monthValue) }
            .toSortedMap()
            .map { (mois, matchsDuMois) -> pointPour(mois, userId, matchsDuMois) }

    private fun progressionParSaison(userId: UUID, matchs: List<MatchEntity>): List<PointProgressionDto> =
        matchs
            .filter { it.saison != null }
            .groupBy { it.saison!!.id!! }
            .entries
            .mapNotNull { (saisonId, matchsDeLaSaison) ->
                val saison = saisonRepository.findById(saisonId).orElse(null) ?: return@mapNotNull null
                saison.dateDebut to pointPour(saison.libelle, userId, matchsDeLaSaison)
            }
            .sortedBy { it.first }
            .map { it.second }

    private fun pointPour(periode: String, userId: UUID, matchs: List<MatchEntity>): PointProgressionDto {
        val evenements = matchs.flatMap { evenementRepository.findAllByMatch_IdOrderByPeriodeAscMinuteAsc(it.id!!) }
        val compositions = matchs.flatMap { compositionRepository.findAllByMatch_Id(it.id!!) }
        val buts = StatistiquesAggregator.butsParJoueur(evenements)[userId] ?: 0
        val presences = StatistiquesAggregator.presencesParJoueur(compositions)[userId] ?: 0
        return PointProgressionDto(periode = periode, buts = buts, presences = presences)
    }

    private fun statistiquesDe(userId: UUID, matchs: List<MatchEntity>): StatistiquesUtilisateurDto {
        val evenements = matchs.flatMap { evenementRepository.findAllByMatch_IdOrderByPeriodeAscMinuteAsc(it.id!!) }
        val compositions = matchs.flatMap { compositionRepository.findAllByMatch_Id(it.id!!) }
        val buts = StatistiquesAggregator.butsParJoueur(evenements)[userId] ?: 0
        val passes = StatistiquesAggregator.passesParJoueur(evenements)[userId] ?: 0
        val presences = StatistiquesAggregator.presencesParJoueur(compositions)[userId] ?: 0
        // Moyenne par match "joué" (présent sur la feuille), pas par match du 2-0 en général.
        val moyenne = if (presences == 0) 0.0 else buts.toDouble() / presences

        return StatistiquesUtilisateurDto(
            utilisateurId = userId.toString(),
            buts = buts,
            passes = passes,
            presences = presences,
            moyenneButsParMatch = moyenne,
        )
    }

    /** Matchs DEUX_ZERO OFFICIEL dans le périmètre demandé (un seul 2-0, un seul club, ou
     * global si ni l'un ni l'autre n'est précisé). */
    private fun matchsConcernes(userId: UUID, deuxZeroId: UUID?, clubId: UUID?): List<MatchEntity> {
        val deuxZeroIds: List<UUID> = when {
            deuxZeroId != null -> listOf(deuxZeroId)
            clubId != null -> campRepository.findAll().filter { it.club?.id == clubId }.mapNotNull { it.deuxZero.id }.distinct()
            else -> null
        } ?: return matchRepository.findAll().filter { it.type == TYPE_MATCH_DEUX_ZERO && it.statut == STATUT_OFFICIEL }

        return deuxZeroIds
            .flatMap { saisonRepository.findAllByDeuxZero_Id(it) }
            .flatMap { matchRepository.findAllBySaison_IdAndStatut(it.id!!, STATUT_OFFICIEL) }
            .filter { it.type == TYPE_MATCH_DEUX_ZERO }
    }
}
