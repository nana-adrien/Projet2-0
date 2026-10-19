package empire.digiprem.projet2_0.server.amical.service

import empire.digiprem.projet2_0.contracts.amical.CreerMatchAmicalRequest
import empire.digiprem.projet2_0.contracts.calendrier.MatchDto
import empire.digiprem.projet2_0.contracts.validation.validateCreerMatchAmicalRequest
import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.calendrier.infra.repository.MatchRepository
import empire.digiprem.projet2_0.server.clubs.infra.repository.AdhesionClubRepository
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ValidationException
import empire.digiprem.projet2_0.server.common.util.orThrow
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException
import java.util.UUID

private const val TYPE_AMICAL = "AMICAL"
private const val STATUT_PROGRAMME = "PROGRAMME"
private const val ROLE_ADMIN = "ADMINISTRATEUR"
private const val STATUT_ACCEPTEE = "ACCEPTEE"

/**
 * CU21 -- Organiser un match amical. Scope serveur limite au scenario nominal mobile (E26) :
 * adversaire "equipe invitee" uniquement (RG-15, F-AMI-03). Le flux A1 "club inscrit + invitation"
 * n'est pas implemente (cf. note CU21 dans uml/tracabilite.json).
 *
 * RG-14 : un match AMICAL n'a jamais de saisonId -> aucun impact sur le classement de la saison
 * (deja garanti par la contrainte SQL sur `match`, non dupliquee ici).
 *
 * Limite structurelle connue (documentee dans uml/tracabilite.json, note CU21) : les CU17/CU18/CU19
 * deja livres resolvent le camp via saisonId, ce qui ne fonctionne pas pour un match AMICAL (pas de
 * Camp au sens UML). La saisie en direct et la validation de ce match ne sont donc pas branchees ici
 * -- hors scope de ce jalon, seule la creation est traitee.
 */
@Service
class AmicalService(
    private val matchRepository: MatchRepository,
    private val adhesionClubRepository: AdhesionClubRepository,
) {

    @Transactional
    fun creerMatchAmical(clubId: UUID, requesterId: UUID, request: CreerMatchAmicalRequest): MatchDto {
        validateCreerMatchAmicalRequest(request).orThrow()
        requireAdminDuClub(clubId, requesterId)

        val match = matchRepository.save(
            MatchEntity(
                clubOrganisateurId = clubId,
                type = TYPE_AMICAL,
                date = parseDate(request.date),
                heure = parseHeure(request.heure),
                statut = STATUT_PROGRAMME,
                dureeMiTempsAppliqueeMinutes = request.dureeMiTempsMinutes,
                nomEquipeInvitee = request.nomEquipeInvitee.trim(),
            )
        )
        return match.toDto()
    }

    private fun requireAdminDuClub(clubId: UUID, requesterId: UUID) {
        val adhesion = adhesionClubRepository.findByUtilisateurIdAndClubId(requesterId, clubId)
        if (adhesion?.role != ROLE_ADMIN || adhesion.statut != STATUT_ACCEPTEE) {
            throw ForbiddenActionException("Seul un administrateur du club peut organiser un match amical")
        }
    }

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
