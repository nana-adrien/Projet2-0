package empire.digiprem.projet2_0.server.calendrier.service

import empire.digiprem.projet2_0.contracts.calendrier.MatchDto
import empire.digiprem.projet2_0.contracts.matchendirect.AjouterEvenementRequest
import empire.digiprem.projet2_0.contracts.matchendirect.EvenementDto
import empire.digiprem.projet2_0.contracts.matchendirect.MatchEnDirectDto
import empire.digiprem.projet2_0.contracts.matchendirect.PeriodeRequest
import empire.digiprem.projet2_0.contracts.validation.validateAjouterEvenementRequest
import empire.digiprem.projet2_0.contracts.validation.validatePeriodeRequest
import empire.digiprem.projet2_0.server.calendrier.infra.entity.EvenementEntity
import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.calendrier.infra.repository.EvenementRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.MatchRepository
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.common.exception.ValidationException
import empire.digiprem.projet2_0.server.common.util.orThrow
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.CampRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.EffectifEngageRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.SaisonRepository
import empire.digiprem.projet2_0.server.notifications.infra.entity.NotificationEntity
import empire.digiprem.projet2_0.server.notifications.infra.repository.NotificationRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

private const val STATUT_PROGRAMME = "PROGRAMME"
private const val STATUT_EN_COURS = "EN_COURS"
private const val STATUT_EN_ATTENTE_DE_VALIDATION = "EN_ATTENTE_DE_VALIDATION"
private const val TYPE_BUT = "BUT"
private const val TYPE_BUT_CSC = "BUT_CONTRE_SON_CAMP"
private const val TYPE_PENALTY_MARQUE = "PENALTY_MARQUE"
private const val TYPE_DEBUT_PERIODE = "DEBUT_PERIODE"
private const val TYPE_FIN_PERIODE = "FIN_PERIODE"
private const val TITRE_COUP_ENVOI = "Coup d'envoi"
private const val TITRE_BUT = "But !"
private const val TITRE_MI_TEMPS = "Mi-temps"
private const val TITRE_CORRECTION = "Correction"
private const val TITRE_FIN_MATCH = "Fin du match"

/** CU17 — Saisir les événements en direct. RG-11 : seul le marqueur désigné (ou son suppléant,
 * CU16) peut saisir. Le mode hors connexion (F-LIV-06) et le passage de relais (F-LIV-08) sont
 * gérés côté mobile (file locale + [AjouterEvenementRequest.clientEventId] pour la dédoublonnage
 * côté serveur) ; la saisie a posteriori (F-LIV-10, A3) n'est pas implémentée (différée). */
@Service
class EvenementService(
    private val evenementRepository: EvenementRepository,
    private val matchRepository: MatchRepository,
    private val saisonRepository: SaisonRepository,
    private val campRepository: CampRepository,
    private val effectifEngageRepository: EffectifEngageRepository,
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
    private val droits: MatchEnDirectDroitsService,
    private val broadcaster: MatchEnDirectBroadcaster,
) {

    @Transactional
    fun demarrerMatch(matchId: UUID, requesterId: UUID): MatchEnDirectDto {
        val match = findMatch(matchId)
        droits.requireMarqueurOuSuppleant(match, requesterId)
        if (match.statut != STATUT_PROGRAMME) {
            throw ValidationException(listOf("statut: le match doit être PROGRAMME pour démarrer (actuel : ${match.statut})"))
        }

        val updated = matchRepository.save(match.copyWith(statut = STATUT_EN_COURS))
        evenementRepository.save(
            EvenementEntity(matchId = matchId, type = TYPE_DEBUT_PERIODE, minute = 0, periode = 1, auteurSaisieId = requesterId)
        )
        notifierMembresDesDeuxCamps(updated, TITRE_COUP_ENVOI, "Le match a commencé.")
        return matchEnDirect(updated).also { broadcaster.broadcast(matchId, it) }
    }

    @Transactional
    fun ajouterEvenement(matchId: UUID, requesterId: UUID, request: AjouterEvenementRequest): MatchEnDirectDto {
        validateAjouterEvenementRequest(request).orThrow()
        val match = findMatch(matchId)
        droits.requireMarqueurOuSuppleant(match, requesterId)
        if (match.statut != STATUT_EN_COURS) {
            throw ValidationException(listOf("statut: le match doit être EN_COURS pour saisir un événement"))
        }

        val clientEventId = request.clientEventId?.let { UUID.fromString(it) }
        val dejaExistant = clientEventId?.let { evenementRepository.findByClientEventId(it) }
        if (dejaExistant != null) {
            // F-LIV-06 : doublon ignoré (resynchronisation hors connexion) — on renvoie l'état actuel sans le rejouer.
            return matchEnDirect(match)
        }

        val campId = request.campId?.let { UUID.fromString(it) }
        val joueurId = request.joueurId?.let { UUID.fromString(it) }
        val joueurSecondaireId = request.joueurSecondaireId?.let { UUID.fromString(it) }

        evenementRepository.save(
            EvenementEntity(
                matchId = matchId, type = request.type, minute = request.minute, periode = request.periode,
                campId = campId, joueurId = joueurId, joueurSecondaireId = joueurSecondaireId,
                auteurSaisieId = requesterId, clientEventId = clientEventId,
            )
        )

        val updatedMatch = when {
            campId == null -> match
            request.type == TYPE_BUT || request.type == TYPE_PENALTY_MARQUE -> incrementerScore(match, campId)
            request.type == TYPE_BUT_CSC -> incrementerScoreCampAdverse(match, campId)
            else -> match
        }
        if (updatedMatch !== match) matchRepository.save(updatedMatch)

        if (request.type == TYPE_BUT || request.type == TYPE_BUT_CSC || request.type == TYPE_PENALTY_MARQUE) {
            val buteur = joueurId?.let { userRepository.findById(it).map { u -> u.pseudo }.orElse(null) }
            notifierMembresDesDeuxCamps(updatedMatch, TITRE_BUT, buteur?.let { "But de $it !" } ?: "But !")
        }

        return matchEnDirect(updatedMatch).also { broadcaster.broadcast(matchId, it) }
    }

    /** F-LIV-04 — correction immédiate ; chaque annulation déclenche une notification rectificative. */
    @Transactional
    fun annulerDernierEvenement(matchId: UUID, requesterId: UUID): MatchEnDirectDto {
        val match = findMatch(matchId)
        droits.requireMarqueurOuSuppleant(match, requesterId)

        val dernier = evenementRepository.findFirstByMatchIdAndAnnuleFalseOrderByHorodatageDesc(matchId)
            ?: throw ResourceNotFoundException("Aucun événement à annuler")
        evenementRepository.save(
            EvenementEntity(
                id = dernier.id, matchId = dernier.matchId, type = dernier.type, minute = dernier.minute,
                periode = dernier.periode, campId = dernier.campId, joueurId = dernier.joueurId,
                joueurSecondaireId = dernier.joueurSecondaireId, auteurSaisieId = dernier.auteurSaisieId,
                clientEventId = dernier.clientEventId, horodatage = dernier.horodatage, annule = true,
            )
        )

        val campId = dernier.campId
        val updatedMatch = when {
            campId == null -> match
            dernier.type == TYPE_BUT || dernier.type == TYPE_PENALTY_MARQUE -> decrementerScore(match, campId)
            dernier.type == TYPE_BUT_CSC -> decrementerScoreCampAdverse(match, campId)
            else -> match
        }
        if (updatedMatch !== match) matchRepository.save(updatedMatch)

        notifierMembresDesDeuxCamps(updatedMatch, TITRE_CORRECTION, "Le dernier événement a été annulé.")
        return matchEnDirect(updatedMatch).also { broadcaster.broadcast(matchId, it) }
    }

    /** F-LIV-02 — clôt la période en cours et lance la suivante. */
    @Transactional
    fun passerMiTemps(matchId: UUID, requesterId: UUID, request: PeriodeRequest): MatchEnDirectDto {
        validatePeriodeRequest(request).orThrow()
        val match = findMatch(matchId)
        droits.requireMarqueurOuSuppleant(match, requesterId)
        if (match.statut != STATUT_EN_COURS) {
            throw ValidationException(listOf("statut: le match doit être EN_COURS"))
        }

        evenementRepository.save(
            EvenementEntity(matchId = matchId, type = TYPE_FIN_PERIODE, minute = 0, periode = request.periodeTerminee, auteurSaisieId = requesterId)
        )
        evenementRepository.save(
            EvenementEntity(matchId = matchId, type = TYPE_DEBUT_PERIODE, minute = 0, periode = request.periodeTerminee + 1, auteurSaisieId = requesterId)
        )
        notifierMembresDesDeuxCamps(match, TITRE_MI_TEMPS, "La période suivante a commencé.")
        return matchEnDirect(match).also { broadcaster.broadcast(matchId, it) }
    }

    /** CU17 étape 6 — fin du temps réglementaire : le match passe en attente de validation (CU19). */
    @Transactional
    fun terminerMatch(matchId: UUID, requesterId: UUID, request: PeriodeRequest): MatchEnDirectDto {
        validatePeriodeRequest(request).orThrow()
        val match = findMatch(matchId)
        droits.requireMarqueurOuSuppleant(match, requesterId)
        if (match.statut != STATUT_EN_COURS) {
            throw ValidationException(listOf("statut: le match doit être EN_COURS"))
        }

        evenementRepository.save(
            EvenementEntity(matchId = matchId, type = TYPE_FIN_PERIODE, minute = 0, periode = request.periodeTerminee, auteurSaisieId = requesterId)
        )
        val updated = matchRepository.save(match.copyWith(statut = STATUT_EN_ATTENTE_DE_VALIDATION))
        notifierMembresDesDeuxCamps(updated, TITRE_FIN_MATCH, "Le match est terminé, en attente de validation.")
        return matchEnDirect(updated).also { broadcaster.broadcast(matchId, it) }
    }

    fun getMatchEnDirect(matchId: UUID): MatchEnDirectDto = matchEnDirect(findMatch(matchId))

    fun listEvenements(matchId: UUID): List<EvenementDto> =
        evenementRepository.findAllByMatchIdOrderByPeriodeAscMinuteAsc(matchId).map { it.toDto() }

    private fun matchEnDirect(match: MatchEntity): MatchEnDirectDto =
        MatchEnDirectDto(match = match.toDto(), evenements = listEvenements(match.id!!))

    private fun incrementerScore(match: MatchEntity, campId: UUID): MatchEntity {
        val (campAId, _) = deuxCampsDe(match)
        return if (campId == campAId) match.copyWith(scoreCampA = (match.scoreCampA ?: 0) + 1)
        else match.copyWith(scoreCampB = (match.scoreCampB ?: 0) + 1)
    }

    private fun decrementerScore(match: MatchEntity, campId: UUID): MatchEntity {
        val (campAId, _) = deuxCampsDe(match)
        return if (campId == campAId) match.copyWith(scoreCampA = ((match.scoreCampA ?: 1) - 1).coerceAtLeast(0))
        else match.copyWith(scoreCampB = ((match.scoreCampB ?: 1) - 1).coerceAtLeast(0))
    }

    /** But contre son camp : le camp qui marque "pour" est celui qui N'A PAS le joueur fautif. */
    private fun incrementerScoreCampAdverse(match: MatchEntity, campDuJoueurFautif: UUID): MatchEntity {
        val (campAId, campBId) = deuxCampsDe(match)
        val campBeneficiaire = if (campDuJoueurFautif == campAId) campBId else campAId
        return incrementerScore(match, campBeneficiaire)
    }

    private fun decrementerScoreCampAdverse(match: MatchEntity, campDuJoueurFautif: UUID): MatchEntity {
        val (campAId, campBId) = deuxCampsDe(match)
        val campBeneficiaire = if (campDuJoueurFautif == campAId) campBId else campAId
        return decrementerScore(match, campBeneficiaire)
    }

    /** RG-04 : toujours exactement 2 camps par 2-0 ; l'ordre (A/B) suit l'ordre de création. */
    private fun deuxCampsDe(match: MatchEntity): Pair<UUID, UUID> {
        val deuxZeroId = deuxZeroIdOf(match)
        val camps = campRepository.findAllByDeuxZeroId(deuxZeroId)
        if (camps.size != 2) throw ResourceNotFoundException("Ce 2-0 n'a pas exactement 2 camps")
        return camps[0].id!! to camps[1].id!!
    }

    private fun deuxZeroIdOf(match: MatchEntity): UUID {
        val saisonId = match.saisonId ?: throw ForbiddenActionException("Ce match n'est pas rattaché à un 2-0")
        return saisonRepository.findById(saisonId).orElseThrow { ResourceNotFoundException("Saison introuvable") }.deuxZeroId
    }

    private fun notifierMembresDesDeuxCamps(match: MatchEntity, titre: String, corps: String) {
        val deuxZeroId = runCatching { deuxZeroIdOf(match) }.getOrNull() ?: return
        val destinataires = campRepository.findAllByDeuxZeroId(deuxZeroId)
            .mapNotNull { it.id }
            .flatMap { effectifEngageRepository.findAllByCampId(it) }
            .filter { it.dateSortie == null }
            .map { it.utilisateurId }
            .distinct()
        destinataires.forEach { userId ->
            notificationRepository.save(NotificationEntity(userId = userId, title = titre, body = corps))
        }
    }

    private fun findMatch(matchId: UUID): MatchEntity =
        matchRepository.findById(matchId).orElseThrow { ResourceNotFoundException("Match introuvable") }

    private fun MatchEntity.copyWith(
        statut: String = this.statut,
        scoreCampA: Int? = this.scoreCampA,
        scoreCampB: Int? = this.scoreCampB,
    ) = MatchEntity(
        id = id, saisonId = saisonId, clubOrganisateurId = clubOrganisateurId, type = type, date = date, heure = heure,
        lieu = lieu, statut = statut, scoreCampA = scoreCampA, scoreCampB = scoreCampB,
        dureeMiTempsAppliqueeMinutes = dureeMiTempsAppliqueeMinutes, nomEquipeInvitee = nomEquipeInvitee,
        marqueurId = marqueurId, marqueurSuppleantId = marqueurSuppleantId,
    )

    private fun MatchEntity.toDto() = MatchDto(
        id = id.toString(), saisonId = saisonId?.toString(), type = type, date = date.toString(), heure = heure.toString(),
        lieu = lieu, statut = statut, scoreCampA = scoreCampA, scoreCampB = scoreCampB,
        dureeMiTempsAppliqueeMinutes = dureeMiTempsAppliqueeMinutes, nomEquipeInvitee = nomEquipeInvitee,
        marqueurId = marqueurId?.toString(), marqueurSuppleantId = marqueurSuppleantId?.toString(),
    )

    private fun EvenementEntity.toDto(): EvenementDto {
        val joueurPseudo = joueurId?.let { userRepository.findById(it).map { u -> u.pseudo }.orElse(null) }
        val joueurSecondairePseudo = joueurSecondaireId?.let { userRepository.findById(it).map { u -> u.pseudo }.orElse(null) }
        return EvenementDto(
            id = id.toString(), matchId = matchId.toString(), type = type, minute = minute, periode = periode,
            campId = campId?.toString(), joueurId = joueurId?.toString(), joueurPseudo = joueurPseudo,
            joueurSecondaireId = joueurSecondaireId?.toString(), joueurSecondairePseudo = joueurSecondairePseudo,
            auteurSaisieId = auteurSaisieId.toString(), horodatage = horodatage.toString(), annule = annule,
        )
    }
}
