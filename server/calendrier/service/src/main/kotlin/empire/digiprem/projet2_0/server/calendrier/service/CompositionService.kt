package empire.digiprem.projet2_0.server.calendrier.service

import empire.digiprem.projet2_0.contracts.matchendirect.CompositionDto
import empire.digiprem.projet2_0.contracts.matchendirect.DesignerCompositionRequest
import empire.digiprem.projet2_0.contracts.matchendirect.DesignerMarqueurRequest
import empire.digiprem.projet2_0.contracts.calendrier.MatchDto
import empire.digiprem.projet2_0.contracts.validation.validateDesignerCompositionRequest
import empire.digiprem.projet2_0.contracts.validation.validateDesignerMarqueurRequest
import empire.digiprem.projet2_0.server.calendrier.infra.entity.CompositionEntity
import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.calendrier.infra.repository.CompositionRepository
import empire.digiprem.projet2_0.server.calendrier.infra.repository.MatchRepository
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.common.util.orThrow
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

/** CU16 — Composer l'équipe avant-match : composition (titulaires/remplaçants) et désignation
 * du marqueur/suppléant, chacun réservé au capitaine (ou vice-capitaine) du camp concerné. */
@Service
class CompositionService(
    private val compositionRepository: CompositionRepository,
    private val matchRepository: MatchRepository,
    private val userRepository: UserRepository,
    private val droits: MatchEnDirectDroitsService,
) {

    @Transactional
    fun designerComposition(matchId: UUID, requesterId: UUID, request: DesignerCompositionRequest): CompositionDto {
        validateDesignerCompositionRequest(request).orThrow()
        val match = findMatch(matchId)
        val deuxZeroId = deuxZeroIdOf(match)
        val target = findUser(UUID.fromString(request.utilisateurId))
        val campId = droits.campDuJoueur(deuxZeroId, target.id!!)
            ?: throw ForbiddenActionException("Ce joueur n'appartient à aucun camp de ce 2-0")
        droits.requireCapitaineOuViceCapitaineDuCamp(campId, requesterId)

        val existing = compositionRepository.findByMatch_IdAndUtilisateur_Id(matchId, target.id!!)
        val saved = compositionRepository.save(
            CompositionEntity(
                id = existing?.id,
                match = match,
                utilisateur = target,
                titulaire = request.titulaire,
                present = request.present,
            )
        )
        return saved.toDto()
    }

    /** @Transactional : [CompositionEntity.utilisateur] (relation LAZY) navigué par [toDto]. */
    @Transactional
    fun listComposition(matchId: UUID): List<CompositionDto> =
        compositionRepository.findAllByMatch_Id(matchId).map { it.toDto() }

    /** F-LIV-01 — un marqueur et un suppléant, désignés par un capitaine du camp concerné. */
    @Transactional
    fun designerMarqueur(matchId: UUID, requesterId: UUID, request: DesignerMarqueurRequest): MatchDto {
        validateDesignerMarqueurRequest(request).orThrow()
        val match = findMatch(matchId)
        val deuxZeroId = deuxZeroIdOf(match)
        val target = findUser(UUID.fromString(request.utilisateurId))
        val campId = droits.campDuJoueur(deuxZeroId, target.id!!)
            ?: throw ForbiddenActionException("Ce joueur n'appartient à aucun camp de ce 2-0")
        droits.requireCapitaineOuViceCapitaineDuCamp(campId, requesterId)

        val updated = matchRepository.save(
            MatchEntity(
                id = match.id,
                saison = match.saison,
                clubOrganisateur = match.clubOrganisateur,
                type = match.type,
                date = match.date,
                heure = match.heure,
                lieu = match.lieu,
                statut = match.statut,
                scoreCampA = match.scoreCampA,
                scoreCampB = match.scoreCampB,
                dureeMiTempsAppliqueeMinutes = match.dureeMiTempsAppliqueeMinutes,
                nomEquipeInvitee = match.nomEquipeInvitee,
                marqueur = if (request.suppleant) match.marqueur else target,
                marqueurSuppleant = if (request.suppleant) target else match.marqueurSuppleant,
                entreEnAttenteValidationA = match.entreEnAttenteValidationA,
            )
        )
        return updated.toDto()
    }

    private fun deuxZeroIdOf(match: MatchEntity): UUID =
        match.saison?.deuxZero?.id ?: throw ForbiddenActionException("Ce match n'est pas rattaché à un 2-0")

    private fun findMatch(matchId: UUID): MatchEntity =
        matchRepository.findById(matchId).orElseThrow { ResourceNotFoundException("Match introuvable") }

    private fun findUser(userId: UUID) =
        userRepository.findById(userId).orElseThrow { ResourceNotFoundException("Utilisateur introuvable") }

    /** Accès à [CompositionEntity.utilisateur] (relation LAZY) : tous les appelants
     * (designerComposition, listComposition) sont @Transactional. */
    private fun CompositionEntity.toDto(): CompositionDto = CompositionDto(
        id = id.toString(), matchId = match.id.toString(), utilisateurId = utilisateur.id.toString(),
        pseudo = utilisateur.pseudo, titulaire = titulaire, present = present,
    )

    private fun MatchEntity.toDto() = MatchDto(
        id = id.toString(), saisonId = saison?.id?.toString(), type = type, date = date.toString(), heure = heure.toString(),
        lieu = lieu, statut = statut, scoreCampA = scoreCampA, scoreCampB = scoreCampB,
        dureeMiTempsAppliqueeMinutes = dureeMiTempsAppliqueeMinutes, nomEquipeInvitee = nomEquipeInvitee,
        marqueurId = marqueur?.id?.toString(), marqueurSuppleantId = marqueurSuppleant?.id?.toString(),
    )
}
