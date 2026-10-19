package empire.digiprem.projet2_0.server.calendrier.service

import empire.digiprem.projet2_0.contracts.calendrier.MatchDto
import empire.digiprem.projet2_0.contracts.litige.ContesterRequest
import empire.digiprem.projet2_0.contracts.litige.RepondreContestationRequest
import empire.digiprem.projet2_0.contracts.litige.ResultatMatchDto
import empire.digiprem.projet2_0.contracts.litige.TrancherLitigeRequest
import empire.digiprem.projet2_0.contracts.litige.ValidationDto
import empire.digiprem.projet2_0.contracts.matchendirect.EvenementDto
import empire.digiprem.projet2_0.contracts.validation.validateContesterRequest
import empire.digiprem.projet2_0.contracts.validation.validateRepondreContestationRequest
import empire.digiprem.projet2_0.contracts.validation.validateTrancherLitigeRequest
import empire.digiprem.projet2_0.server.calendrier.infra.entity.EvenementEntity
import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.calendrier.infra.entity.ValidationEntity
import empire.digiprem.projet2_0.server.calendrier.infra.repository.EvenementRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.MatchRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.ValidationRepository
import empire.digiprem.projet2_0.server.common.exception.ConflictException
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.common.exception.ValidationException
import empire.digiprem.projet2_0.server.common.util.orThrow
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.CampRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.DeuxZeroRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.EffectifEngageRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.SaisonRepository
import empire.digiprem.projet2_0.server.notifications.infra.entity.NotificationEntity
import empire.digiprem.projet2_0.server.notifications.infra.repository.NotificationRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.UUID

private const val STATUT_EN_ATTENTE_DE_VALIDATION = "EN_ATTENTE_DE_VALIDATION"
private const val STATUT_OFFICIEL = "OFFICIEL"
private const val STATUT_EN_LITIGE = "EN_LITIGE"
private const val STATUT_ANNULE = "ANNULE"
private const val DECISION_VALIDE = "VALIDE"
private const val DECISION_CONTESTE = "CONTESTE"
private const val DECISION_ANNULER_MATCH = "ANNULER_MATCH"
private const val TITRE_RESULTAT_OFFICIALISE = "Résultat officialisé"
private const val TITRE_CONTESTATION_RECUE = "Contestation reçue"
private const val TITRE_LITIGE_OUVERT = "Litige ouvert"
private const val TITRE_RESULTAT_CORRIGE = "Résultat corrigé"
private const val TITRE_LITIGE_ARBITRE = "Litige arbitré"
private const val TITRE_VALIDATION_AUTO = "Validation automatique"

/** CU19 — Valider ou contester le résultat d'un match (signature par camp). CU20 — Arbitrer un
 * litige (organisateur). RG-12 : passé le délai [deuxZero.delaiValidationAutomatiqueHeures] après
 * le passage à EN_ATTENTE_DE_VALIDATION, le résultat est validé automatiquement SAUF si une
 * contestation est en cours — vérification paresseuse (pas de @Scheduled), déclenchée à chaque
 * lecture/action sur le match via [verifierExpirationSiNecessaire].
 *
 * Limitations assumées (documentées en tête de rapport de la tâche) :
 * - [repondreContestation] (acceptation) et [trancherLitige] (GARDER_RESULTAT_SAISI /
 *   APPLIQUER_CORRECTION) n'ajustent jamais le score : [ContesterRequest.correctionProposee] et
 *   [TrancherLitigeRequest] sont des textes libres non structurés (fiche UML), aucun mécanisme de
 *   correction d'événements ne permet de les traduire en score.
 * - Si une contestation reste sans réponse de l'adverse, le match reste EN_ATTENTE_DE_VALIDATION
 *   indéfiniment même après expiration du délai RG-12 : aucune trajectoire d'auto-escalade vers
 *   EN_LITIGE par simple expiration n'est prévue par uml/domain/commun/etats/etats-match.puml.
 */
@Service
class ValidationService(
    private val matchRepository: MatchRepository,
    private val evenementRepository: EvenementRepository,
    private val validationRepository: ValidationRepository,
    private val saisonRepository: SaisonRepository,
    private val campRepository: CampRepository,
    private val deuxZeroRepository: DeuxZeroRepository,
    private val effectifEngageRepository: EffectifEngageRepository,
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
    private val droits: ValidationDroitsService,
    private val arbitrageDroits: MatchArbitrageDroitsService,
) {

    @Transactional
    fun getResultat(matchId: UUID): ResultatMatchDto {
        verifierExpirationSiNecessaire(findMatch(matchId))
        return resultatDe(findMatch(matchId))
    }

    /** CU19 étape 2 — signature VALIDE pour le camp du requester. */
    @Transactional
    fun valider(matchId: UUID, requesterId: UUID): ResultatMatchDto {
        verifierExpirationSiNecessaire(findMatch(matchId))
        val match = findMatch(matchId)
        match.requireEnAttenteDeValidation()

        val deuxZeroId = deuxZeroIdOf(match)
        val campId = resolveCampSignataireOuThrow(deuxZeroId, requesterId)
        requireAucuneContestationAdverseEnAttente(matchId, campId)

        upsertSignature(matchId, campId, requesterId, decision = DECISION_VALIDE, commentaire = null, correctionProposee = null)

        val validationsApres = validationRepository.findAllByMatchId(matchId).filter { it.campId != null }
        val adverseApres = validationsApres.firstOrNull { it.campId != campId }
        if (adverseApres != null && adverseApres.decision == DECISION_VALIDE) {
            val updated = matchRepository.save(match.copyWith(statut = STATUT_OFFICIEL))
            notifierMembresDesDeuxCamps(updated, TITRE_RESULTAT_OFFICIALISE, "Le résultat du match a été officialisé.")
        }

        return getResultat(matchId)
    }

    /** CU19 A1 — contestation du résultat par le camp du requester. */
    @Transactional
    fun contester(matchId: UUID, requesterId: UUID, request: ContesterRequest): ResultatMatchDto {
        validateContesterRequest(request).orThrow()
        verifierExpirationSiNecessaire(findMatch(matchId))
        val match = findMatch(matchId)
        match.requireEnAttenteDeValidation()

        val deuxZeroId = deuxZeroIdOf(match)
        val campId = resolveCampSignataireOuThrow(deuxZeroId, requesterId)
        requireAucuneContestationAdverseEnAttente(matchId, campId)

        upsertSignature(
            matchId, campId, requesterId, decision = DECISION_CONTESTE,
            commentaire = request.commentaire, correctionProposee = request.correctionProposee,
        )

        val validationsApres = validationRepository.findAllByMatchId(matchId).filter { it.campId != null }
        val adverseApres = validationsApres.firstOrNull { it.campId != campId }
        if (adverseApres != null && adverseApres.decision == DECISION_CONTESTE) {
            // Contestation mutuelle : aucune négociation possible, litige ouvert.
            val updated = matchRepository.save(match.copyWith(statut = STATUT_EN_LITIGE))
            notifierMembresDesDeuxCamps(updated, TITRE_LITIGE_OUVERT, "Un litige a été ouvert sur ce match, l'organisateur va trancher.", inclureOrganisateur = true)
        } else {
            // Adverse absent ou déjà VALIDE (avant ma contestation) : sa réponse explicite est
            // désormais nécessaire via repondre-contestation.
            notifierMembresDesDeuxCamps(match, TITRE_CONTESTATION_RECUE, "Une contestation a été déposée, une réponse est attendue.")
        }

        return getResultat(matchId)
    }

    /** CU19 A1 — réponse du camp adverse à une contestation en attente. */
    @Transactional
    fun repondreContestation(matchId: UUID, requesterId: UUID, request: RepondreContestationRequest): ResultatMatchDto {
        validateRepondreContestationRequest(request).orThrow()
        val match = findMatch(matchId)
        if (match.statut != STATUT_EN_ATTENTE_DE_VALIDATION) {
            throw ConflictException("Ce match n'est plus en attente de validation")
        }

        val deuxZeroId = deuxZeroIdOf(match)
        val monCampId = resolveCampSignataireOuThrow(deuxZeroId, requesterId)

        val validations = validationRepository.findAllByMatchId(matchId).filter { it.campId != null }
        validations.firstOrNull { it.campId != monCampId && it.decision == DECISION_CONTESTE }
            ?: throw ValidationException(listOf("aucune contestation à traiter"))

        if (request.accepter) {
            // Score INCHANGÉ : voir limitation documentée en tête de classe.
            val updated = matchRepository.save(match.copyWith(statut = STATUT_OFFICIEL))
            upsertSignature(matchId, monCampId, requesterId, decision = DECISION_VALIDE, commentaire = request.commentaire, correctionProposee = null)
            notifierMembresDesDeuxCamps(updated, TITRE_RESULTAT_CORRIGE, "Le résultat du match a été corrigé et officialisé.")
        } else {
            upsertSignature(matchId, monCampId, requesterId, decision = DECISION_CONTESTE, commentaire = request.commentaire, correctionProposee = null)
            val updated = matchRepository.save(match.copyWith(statut = STATUT_EN_LITIGE))
            notifierMembresDesDeuxCamps(updated, TITRE_LITIGE_OUVERT, "Un litige a été ouvert — contestation refusée.", inclureOrganisateur = true)
        }

        return getResultat(matchId)
    }

    /** CU20 — l'organisateur du 2-0 tranche un litige ouvert. */
    @Transactional
    fun trancherLitige(matchId: UUID, requesterId: UUID, request: TrancherLitigeRequest): ResultatMatchDto {
        val match = findMatch(matchId)
        if (match.statut != STATUT_EN_LITIGE) {
            throw ValidationException(listOf("statut: le match doit être EN_LITIGE pour être arbitré"))
        }

        val deuxZeroId = deuxZeroIdOf(match)
        arbitrageDroits.requireOrganisateur(deuxZeroId, requesterId)
        validateTrancherLitigeRequest(request).orThrow()

        // GARDER_RESULTAT_SAISI / APPLIQUER_CORRECTION : score INCHANGÉ (voir limitation en tête
        // de classe, TrancherLitigeRequest n'a pas de champ de score).
        val nouveauStatut = if (request.decision == DECISION_ANNULER_MATCH) STATUT_ANNULE else STATUT_OFFICIEL
        val updated = matchRepository.save(match.copyWith(statut = nouveauStatut))

        // Ligne d'audit CU20 : campId = null signale une ligne d'arbitrage (jamais une signature
        // de camp). correctionProposee réutilisé pour stocker le nom de la DecisionLitige choisie,
        // faute de colonne dédiée (décision actée côté kna-concepteur dans uml/tracabilite.json).
        validationRepository.save(
            ValidationEntity(
                matchId = matchId, campId = null, signataireId = requesterId,
                decision = DECISION_VALIDE, commentaire = request.motif, correctionProposee = request.decision,
            )
        )

        notifierMembresDesDeuxCamps(updated, TITRE_LITIGE_ARBITRE, "Le litige a été arbitré par l'organisateur.")
        return getResultat(matchId)
    }

    /** RG-12 — vérification paresseuse de l'échéance de validation automatique (pas de @Scheduled).
     * Persiste et notifie si l'échéance est dépassée ET qu'aucune contestation n'est en cours. */
    @Transactional
    fun verifierExpirationSiNecessaire(match: MatchEntity) {
        if (match.statut != STATUT_EN_ATTENTE_DE_VALIDATION) return
        val entreEnAttente = match.entreEnAttenteValidationA ?: return

        val deuxZeroId = deuxZeroIdOf(match)
        val deuxZero = deuxZeroRepository.findById(deuxZeroId).orElseThrow { ResourceNotFoundException("2-0 introuvable") }
        val echeance = entreEnAttente.plus(Duration.ofHours(deuxZero.delaiValidationAutomatiqueHeures.toLong()))
        if (Instant.now().isBefore(echeance)) return

        val validations = validationRepository.findAllByMatchId(match.id!!).filter { it.campId != null }
        val auMoinsUneContestation = validations.any { it.decision == DECISION_CONTESTE }
        if (!auMoinsUneContestation) {
            val updated = matchRepository.save(match.copyWith(statut = STATUT_OFFICIEL))
            notifierMembresDesDeuxCamps(updated, TITRE_VALIDATION_AUTO, "Le résultat a été validé automatiquement à l'expiration du délai.")
        }
        // Sinon : contestation jamais résolue par l'adverse -> reste EN_ATTENTE_DE_VALIDATION
        // indéfiniment (limitation assumée, voir KDoc de la classe).
    }

    private fun resolveCampSignataireOuThrow(deuxZeroId: UUID, requesterId: UUID): UUID =
        droits.campDuCapitaineOuViceCapitaine(deuxZeroId, requesterId)
            ?: throw ForbiddenActionException("Seul un capitaine ou vice-capitaine d'un des deux camps peut signer la validation")

    private fun requireAucuneContestationAdverseEnAttente(matchId: UUID, monCampId: UUID) {
        val validations = validationRepository.findAllByMatchId(matchId).filter { it.campId != null }
        val maLigne = validations.firstOrNull { it.campId == monCampId }
        val adverse = validations.firstOrNull { it.campId != monCampId }
        if (adverse != null && adverse.decision == DECISION_CONTESTE && maLigne == null) {
            throw ValidationException(listOf("une contestation est en attente de votre réponse, utilisez repondre-contestation"))
        }
    }

    private fun upsertSignature(
        matchId: UUID,
        campId: UUID,
        signataireId: UUID,
        decision: String,
        commentaire: String?,
        correctionProposee: String?,
    ) {
        val existante = validationRepository.findByMatchIdAndCampId(matchId, campId)
        validationRepository.save(
            ValidationEntity(
                id = existante?.id, matchId = matchId, campId = campId, signataireId = signataireId,
                decision = decision, commentaire = commentaire, correctionProposee = correctionProposee,
                horodatage = Instant.now(),
            )
        )
    }

    private fun MatchEntity.requireEnAttenteDeValidation() {
        if (statut == STATUT_OFFICIEL || statut == STATUT_EN_LITIGE || statut == STATUT_ANNULE) {
            throw ConflictException("Ce match n'est plus en attente de validation")
        }
        if (statut != STATUT_EN_ATTENTE_DE_VALIDATION) {
            throw ValidationException(listOf("statut: le match doit être EN_ATTENTE_DE_VALIDATION"))
        }
    }

    private fun resultatDe(match: MatchEntity): ResultatMatchDto = ResultatMatchDto(
        match = match.toDto(),
        evenements = evenementRepository.findAllByMatchIdOrderByPeriodeAscMinuteAsc(match.id!!).map { it.toDto() },
        validations = validationRepository.findAllByMatchId(match.id!!).filter { it.campId != null }.map { it.toValidationDto() },
    )

    private fun notifierMembresDesDeuxCamps(match: MatchEntity, titre: String, corps: String, inclureOrganisateur: Boolean = false) {
        val deuxZeroId = runCatching { deuxZeroIdOf(match) }.getOrNull() ?: return
        val destinataires = campRepository.findAllByDeuxZeroId(deuxZeroId)
            .mapNotNull { it.id }
            .flatMap { effectifEngageRepository.findAllByCampId(it) }
            .filter { it.dateSortie == null }
            .map { it.utilisateurId }
            .toMutableSet()
        if (inclureOrganisateur) {
            deuxZeroRepository.findById(deuxZeroId).orElse(null)?.organisateurId?.let { destinataires += it }
        }
        destinataires.forEach { userId ->
            notificationRepository.save(NotificationEntity(userId = userId, title = titre, body = corps))
        }
    }

    private fun findMatch(matchId: UUID): MatchEntity =
        matchRepository.findById(matchId).orElseThrow { ResourceNotFoundException("Match introuvable") }

    private fun deuxZeroIdOf(match: MatchEntity): UUID {
        val saisonId = match.saisonId ?: throw ForbiddenActionException("Ce match n'est pas rattaché à un 2-0")
        return saisonRepository.findById(saisonId).orElseThrow { ResourceNotFoundException("Saison introuvable") }.deuxZeroId
    }

    private fun MatchEntity.copyWith(
        statut: String = this.statut,
    ) = MatchEntity(
        id = id, saisonId = saisonId, clubOrganisateurId = clubOrganisateurId, type = type, date = date, heure = heure,
        lieu = lieu, statut = statut, scoreCampA = scoreCampA, scoreCampB = scoreCampB,
        dureeMiTempsAppliqueeMinutes = dureeMiTempsAppliqueeMinutes, nomEquipeInvitee = nomEquipeInvitee,
        marqueurId = marqueurId, marqueurSuppleantId = marqueurSuppleantId,
        entreEnAttenteValidationA = entreEnAttenteValidationA,
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

    private fun ValidationEntity.toValidationDto(): ValidationDto {
        val pseudo = userRepository.findById(signataireId).map { it.pseudo }.orElse("")
        return ValidationDto(
            id = id.toString(), matchId = matchId.toString(), campId = campId!!.toString(),
            signataireId = signataireId.toString(), signatairePseudo = pseudo, decision = decision,
            commentaire = commentaire, correctionProposee = correctionProposee, horodatage = horodatage.toString(),
        )
    }
}
