package empire.digiprem.projet2_0.server.deuxzero.service

import empire.digiprem.projet2_0.contracts.deuxzero.AddEffectifRequest
import empire.digiprem.projet2_0.contracts.deuxzero.EffectifDto
import empire.digiprem.projet2_0.contracts.deuxzero.TransferPlayerRequest
import empire.digiprem.projet2_0.server.clubs.infra.repository.AdhesionClubRepository
import empire.digiprem.projet2_0.server.common.exception.ConflictException
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.deuxzero.infra.entity.CampEntity
import empire.digiprem.projet2_0.server.deuxzero.infra.entity.EffectifEngageEntity
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.CampRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.EffectifEngageRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

private const val ROLE_ADMIN = "ADMINISTRATEUR"
private const val STATUT_ACCEPTEE = "ACCEPTEE"

/** CU10 — Composer un effectif engagé ; CU11 — Transférer un joueur de camp. */
@Service
class EffectifService(
    private val effectifRepository: EffectifEngageRepository,
    private val campRepository: CampRepository,
    private val adhesionClubRepository: AdhesionClubRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun addMember(campId: UUID, requesterId: UUID, request: AddEffectifRequest): EffectifDto {
        val camp = findCamp(campId)
        requireClubAdmin(camp.clubId, requesterId)
        val targetId = UUID.fromString(request.utilisateurId)

        // RG-08 : le joueur doit être membre accepté du club porteur du camp.
        val membership = camp.clubId?.let { adhesionClubRepository.findByUtilisateurIdAndClubId(targetId, it) }
        if (membership == null || membership.statut != STATUT_ACCEPTEE) {
            throw ForbiddenActionException("Ce joueur n'est pas membre accepté du club")
        }

        // RG-06 : pas déjà dans un autre camp du même 2-0.
        requireNotInAnotherCampOfSameDeuxZero(targetId, camp)

        val effectif = effectifRepository.save(
            EffectifEngageEntity(
                campId = campId,
                utilisateurId = targetId,
                numeroMaillot = request.numeroMaillot,
                dateEntree = LocalDate.now(),
            )
        )
        return effectif.toDto()
    }

    fun listEffectif(campId: UUID): List<EffectifDto> =
        effectifRepository.findAllByCampId(campId).filter { it.dateSortie == null }.map { it.toDto() }

    /** CU11 — clôture l'ancien effectif (dateSortie), crée le nouveau (mêmes contrôles que CU10). */
    @Transactional
    fun transferPlayer(requesterId: UUID, request: TransferPlayerRequest): EffectifDto {
        val targetId = UUID.fromString(request.utilisateurId)
        val nouveauCampId = UUID.fromString(request.nouveauCampId)
        val nouveauCamp = findCamp(nouveauCampId)
        requireClubAdmin(nouveauCamp.clubId, requesterId)

        val ancienneAppartenance = effectifRepository.findAllByUtilisateurIdAndDateSortieIsNull(targetId)
            .firstOrNull { effectifEngageCamp(it) == nouveauCamp.deuxZeroId }
        // RG-09 : clôture l'appartenance au camp d'origine, les stats restent sur l'ancien camp.
        ancienneAppartenance?.let {
            effectifRepository.save(
                EffectifEngageEntity(
                    id = it.id, campId = it.campId, utilisateurId = it.utilisateurId,
                    numeroMaillot = it.numeroMaillot, dateEntree = it.dateEntree, dateSortie = LocalDate.now(),
                )
            )
        }

        val membership = nouveauCamp.clubId?.let { adhesionClubRepository.findByUtilisateurIdAndClubId(targetId, it) }
        if (membership == null || membership.statut != STATUT_ACCEPTEE) {
            throw ForbiddenActionException("Ce joueur n'est pas membre accepté du club")
        }
        requireNotInAnotherCampOfSameDeuxZero(targetId, nouveauCamp)

        return effectifRepository.save(
            EffectifEngageEntity(campId = nouveauCampId, utilisateurId = targetId, dateEntree = LocalDate.now())
        ).toDto()
    }

    private fun effectifEngageCamp(effectif: EffectifEngageEntity): UUID = findCamp(effectif.campId).deuxZeroId

    private fun requireNotInAnotherCampOfSameDeuxZero(userId: UUID, camp: CampEntity) {
        val dejaPresent = effectifRepository.findAllByUtilisateurIdAndDateSortieIsNull(userId)
            .any { it.campId != camp.id && effectifEngageCamp(it) == camp.deuxZeroId }
        if (dejaPresent) {
            throw ConflictException("Ce joueur appartient déjà à l'autre camp de ce 2-0")
        }
    }

    private fun findCamp(campId: UUID): CampEntity =
        campRepository.findById(campId).orElseThrow { ResourceNotFoundException("Camp introuvable") }

    private fun requireClubAdmin(clubId: UUID?, userId: UUID) {
        val adhesion = clubId?.let { adhesionClubRepository.findByUtilisateurIdAndClubId(userId, it) }
        if (adhesion?.role != ROLE_ADMIN || adhesion.statut != STATUT_ACCEPTEE) {
            throw ForbiddenActionException("Seul un administrateur du club peut faire cette action")
        }
    }

    private fun EffectifEngageEntity.toDto(): EffectifDto {
        val pseudo = userRepository.findById(utilisateurId).map { it.pseudo }.orElse("")
        return EffectifDto(
            id = id.toString(), campId = campId.toString(), utilisateurId = utilisateurId.toString(),
            pseudo = pseudo, numeroMaillot = numeroMaillot, dateEntree = dateEntree.toString(),
            dateSortie = dateSortie?.toString(),
        )
    }
}
