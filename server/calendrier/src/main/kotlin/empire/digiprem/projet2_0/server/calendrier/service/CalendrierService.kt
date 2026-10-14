package empire.digiprem.projet2_0.server.calendrier.service

import empire.digiprem.projet2_0.contracts.calendrier.AjouterMatchExceptionnelRequest
import empire.digiprem.projet2_0.contracts.calendrier.MatchDto
import empire.digiprem.projet2_0.contracts.calendrier.ModifierMatchRequest
import empire.digiprem.projet2_0.contracts.calendrier.StatutMatchModifiable
import empire.digiprem.projet2_0.contracts.validation.validateAjouterMatchExceptionnelRequest
import empire.digiprem.projet2_0.contracts.validation.validateModifierMatchRequest
import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.calendrier.infra.repository.MatchRepository
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
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException
import java.util.UUID

private const val TYPE_DEUX_ZERO = "DEUX_ZERO"
private const val STATUT_PROGRAMME = "PROGRAMME"
private const val STATUT_SAISON_EN_COURS = "EN_COURS"
private const val TITRE_CHANGEMENT_PROGRAMME = "Changement de programme"

/** Nombre maximum d'occurrences générées en une fois par [CalendrierService.genererCalendrier],
 * borné plus tôt par la fin de saison si elle est atteinte avant (F-CAL-01). */
private const val MAX_OCCURRENCES_GENERATION = 8

/**
 * CU14 — Générer et gérer le calendrier des matchs d'un 2-0.
 *
 * Convention retenue pour [empire.digiprem.projet2_0.server.deuxzero.infra.entity.DeuxZeroEntity.joursHabituels]
 * (aucun usage existant ne la fixait, cf. `uml/modele.json` qui type le champ `List<DayOfWeek>`) :
 * chaque entrée est le nom exact d'une constante [DayOfWeek] (ex. "MONDAY", "SATURDAY").
 */
@Service
class CalendrierService(
    private val matchRepository: MatchRepository,
    private val saisonRepository: SaisonRepository,
    private val deuxZeroRepository: DeuxZeroRepository,
    private val campRepository: CampRepository,
    private val effectifEngageRepository: EffectifEngageRepository,
    private val notificationRepository: NotificationRepository,
    private val droits: CalendrierDroitsService,
) {

    /** Idempotent : ne recrée jamais un match déjà présent pour (saisonId, date, heure). */
    @Transactional
    fun genererCalendrier(deuxZeroId: UUID, requesterId: UUID): List<MatchDto> {
        droits.requireGestionnaireCalendrier(deuxZeroId, requesterId)
        val deuxZero = deuxZeroRepository.findById(deuxZeroId).orElseThrow { ResourceNotFoundException("2-0 introuvable") }
        val saison = saisonEnCours(deuxZeroId)
        val saisonId = saison.id ?: throw ResourceNotFoundException("Saison introuvable")

        val heureHabituelle = deuxZero.heureHabituelle
        if (deuxZero.joursHabituels.isEmpty() || heureHabituelle == null) {
            throw ValidationException(
                listOf("deuxZero: les jours habituels et l'heure habituelle doivent être définis avant de générer le calendrier")
            )
        }
        val joursCibles = deuxZero.joursHabituels.map { jour ->
            try {
                DayOfWeek.valueOf(jour)
            } catch (e: IllegalArgumentException) {
                throw ValidationException(listOf("joursHabituels: valeur inconnue '$jour', attendu l'un de ${DayOfWeek.entries.joinToString()}"))
            }
        }.toSet()

        val creneauxExistants = matchRepository.findAllBySaisonId(saisonId).map { it.date to it.heure }.toSet()

        val nouveauxMatchs = mutableListOf<MatchEntity>()
        var curseur = LocalDate.now().minusDays(1)
        var occurrencesTrouvees = 0
        while (occurrencesTrouvees < MAX_OCCURRENCES_GENERATION) {
            curseur = curseur.plusDays(1)
            if (saison.dateFin != null && curseur.isAfter(saison.dateFin)) break
            if (curseur.dayOfWeek in joursCibles) {
                occurrencesTrouvees++
                val creneau = curseur to heureHabituelle
                if (creneau !in creneauxExistants) {
                    nouveauxMatchs += MatchEntity(
                        saisonId = saisonId,
                        type = TYPE_DEUX_ZERO,
                        date = curseur,
                        heure = heureHabituelle,
                        lieu = deuxZero.lieu,
                        statut = STATUT_PROGRAMME,
                    )
                }
            }
        }
        return matchRepository.saveAll(nouveauxMatchs).map { it.toDto() }
    }

    @Transactional
    fun ajouterMatchExceptionnel(deuxZeroId: UUID, requesterId: UUID, request: AjouterMatchExceptionnelRequest): MatchDto {
        validateAjouterMatchExceptionnelRequest(request).orThrow()
        droits.requireGestionnaireCalendrier(deuxZeroId, requesterId)
        val deuxZero = deuxZeroRepository.findById(deuxZeroId).orElseThrow { ResourceNotFoundException("2-0 introuvable") }
        val saison = saisonEnCours(deuxZeroId)

        val match = matchRepository.save(
            MatchEntity(
                saisonId = saison.id,
                type = TYPE_DEUX_ZERO,
                date = parseDate(request.date),
                heure = parseHeure(request.heure),
                lieu = request.lieu ?: deuxZero.lieu,
                statut = STATUT_PROGRAMME,
            )
        )
        return match.toDto()
    }

    /** Lecture simple — matchs de la saison EN_COURS du 2-0 (future évolution possible : paramètre
     * saisonId optionnel pour consulter l'historique). */
    fun listMatchs(deuxZeroId: UUID): List<MatchDto> {
        val saison = saisonRepository.findFirstByDeuxZeroIdAndStatutOrderByDateDebutDesc(deuxZeroId, STATUT_SAISON_EN_COURS)
            ?: return emptyList()
        val saisonId = saison.id ?: return emptyList()
        return matchRepository.findAllBySaisonId(saisonId)
            .sortedWith(compareBy({ it.date }, { it.heure }))
            .map { it.toDto() }
    }

    @Transactional
    fun modifierMatch(matchId: UUID, requesterId: UUID, request: ModifierMatchRequest): MatchDto {
        validateModifierMatchRequest(request).orThrow()
        val match = matchRepository.findById(matchId).orElseThrow { ResourceNotFoundException("Match introuvable") }
        val saisonId = match.saisonId
            ?: throw ForbiddenActionException("Ce match n'est pas rattaché à une saison de 2-0 (modification hors scope Jalon 2)")
        val deuxZeroId = droits.requireGestionnaireCalendrierPourSaison(saisonId, requesterId)

        val estReportOuAnnulation = request.nouveauStatut == StatutMatchModifiable.REPORTE.name ||
            request.nouveauStatut == StatutMatchModifiable.ANNULE.name
        if (estReportOuAnnulation && request.motif.isNullOrBlank()) {
            throw ValidationException(listOf("motif: obligatoire pour un report ou une annulation (RG-CAL)"))
        }

        val updated = matchRepository.save(
            MatchEntity(
                id = match.id,
                saisonId = match.saisonId,
                clubOrganisateurId = match.clubOrganisateurId,
                type = match.type,
                date = request.nouvelleDate?.let { parseDate(it) } ?: match.date,
                heure = request.nouvelleHeure?.let { parseHeure(it) } ?: match.heure,
                lieu = match.lieu,
                statut = request.nouveauStatut,
                scoreCampA = match.scoreCampA,
                scoreCampB = match.scoreCampB,
                dureeMiTempsAppliqueeMinutes = request.dureeMiTempsAppliqueeMinutes ?: match.dureeMiTempsAppliqueeMinutes,
                nomEquipeInvitee = match.nomEquipeInvitee,
                marqueurId = match.marqueurId,
            )
        )

        // Le motif n'est volontairement pas persisté sur Match (aucune colonne prévue en base,
        // lacune UML déjà signalée) — il ne sert que de corps de notification.
        val motif = request.motif
        if (!motif.isNullOrBlank()) {
            notifierChangementDeProgramme(deuxZeroId, motif)
        }
        return updated.toDto()
    }

    private fun notifierChangementDeProgramme(deuxZeroId: UUID, motif: String) {
        val destinataires = campRepository.findAllByDeuxZeroId(deuxZeroId)
            .mapNotNull { it.id }
            .flatMap { effectifEngageRepository.findAllByCampId(it) }
            .filter { it.dateSortie == null }
            .map { it.utilisateurId }
            .distinct()
        destinataires.forEach { userId ->
            notificationRepository.save(NotificationEntity(userId = userId, title = TITRE_CHANGEMENT_PROGRAMME, body = motif))
        }
    }

    private fun saisonEnCours(deuxZeroId: UUID) =
        saisonRepository.findFirstByDeuxZeroIdAndStatutOrderByDateDebutDesc(deuxZeroId, STATUT_SAISON_EN_COURS)
            ?: throw ResourceNotFoundException("Aucune saison en cours pour ce 2-0")

    private fun parseDate(value: String): LocalDate = try {
        LocalDate.parse(value)
    } catch (e: DateTimeParseException) {
        throw ValidationException(listOf("date: format invalide, attendu AAAA-MM-JJ"))
    }

    private fun parseHeure(value: String): LocalTime = try {
        LocalTime.parse(value)
    } catch (e: DateTimeParseException) {
        throw ValidationException(listOf("heure: format invalide, attendu HH:MM ou HH:MM:SS"))
    }

    private fun MatchEntity.toDto() = MatchDto(
        id = id.toString(),
        saisonId = saisonId?.toString(),
        type = type,
        date = date.toString(),
        heure = heure.toString(),
        lieu = lieu,
        statut = statut,
        scoreCampA = scoreCampA,
        scoreCampB = scoreCampB,
        dureeMiTempsAppliqueeMinutes = dureeMiTempsAppliqueeMinutes,
        nomEquipeInvitee = nomEquipeInvitee,
        marqueurId = marqueurId?.toString(),
        marqueurSuppleantId = marqueurSuppleantId?.toString(),
    )
}
