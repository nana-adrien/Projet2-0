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
        requireClubAdmin(camp.club?.id, requesterId)
        val target = findUser(UUID.fromString(request.utilisateurId))

        // RG-08 : le joueur doit être membre accepté du club porteur du camp.
        val membership = camp.club?.id?.let { adhesionClubRepository.findByUtilisateur_IdAndClub_Id(target.id!!, it) }
        if (membership == null || membership.statut != STATUT_ACCEPTEE) {
            throw ForbiddenActionException("Ce joueur n'est pas membre accepté du club")
        }

        // RG-06 : pas déjà dans un autre camp du même 2-0.
        requireNotInAnotherCampOfSameDeuxZero(target.id!!, camp)

        val effectif = effectifRepository.save(
            EffectifEngageEntity(
                camp = camp,
                utilisateur = target,
                numeroMaillot = request.numeroMaillot,
                dateEntree = LocalDate.now(),
            )
        )
        return effectif.toDto()
    }

    /** @Transactional : [EffectifEngageEntity.utilisateur] (relation LAZY) est désormais navigué
     * directement par [toDto] au lieu d'un lookup manuel par repository. */
    @Transactional
    fun listEffectif(campId: UUID): List<EffectifDto> =
        effectifRepository.findAllByCamp_Id(campId).filter { it.dateSortie == null }.map { it.toDto() }

    /** CU11 — clôture l'ancien effectif (dateSortie), crée le nouveau (mêmes contrôles que CU10). */
    @Transactional
    fun transferPlayer(requesterId: UUID, request: TransferPlayerRequest): EffectifDto {
        val target = findUser(UUID.fromString(request.utilisateurId))
        val nouveauCampId = UUID.fromString(request.nouveauCampId)
        val nouveauCamp = findCamp(nouveauCampId)
        requireClubAdmin(nouveauCamp.club?.id, requesterId)

        val ancienneAppartenance = effectifRepository.findAllByUtilisateur_IdAndDateSortieIsNull(target.id!!)
            .firstOrNull { effectifEngageCamp(it) == nouveauCamp.deuxZero.id }
        // RG-09 : clôture l'appartenance au camp d'origine, les stats restent sur l'ancien camp.
        ancienneAppartenance?.let {
            effectifRepository.save(
                EffectifEngageEntity(
                    id = it.id, camp = it.camp, utilisateur = it.utilisateur,
                    numeroMaillot = it.numeroMaillot, dateEntree = it.dateEntree, dateSortie = LocalDate.now(),
                )
            )
        }

        val membership = nouveauCamp.club?.id?.let { adhesionClubRepository.findByUtilisateur_IdAndClub_Id(target.id!!, it) }
        if (membership == null || membership.statut != STATUT_ACCEPTEE) {
            throw ForbiddenActionException("Ce joueur n'est pas membre accepté du club")
        }
        requireNotInAnotherCampOfSameDeuxZero(target.id!!, nouveauCamp)

        return effectifRepository.save(
            EffectifEngageEntity(camp = nouveauCamp, utilisateur = target, dateEntree = LocalDate.now())
        ).toDto()
    }

    private fun effectifEngageCamp(effectif: EffectifEngageEntity): UUID? = effectif.camp.deuxZero.id

    private fun requireNotInAnotherCampOfSameDeuxZero(userId: UUID, camp: CampEntity) {
        val dejaPresent = effectifRepository.findAllByUtilisateur_IdAndDateSortieIsNull(userId)
            .any { it.camp.id != camp.id && effectifEngageCamp(it) == camp.deuxZero.id }
        if (dejaPresent) {
            throw ConflictException("Ce joueur appartient déjà à l'autre camp de ce 2-0")
        }
    }

    private fun findCamp(campId: UUID): CampEntity =
        campRepository.findById(campId).orElseThrow { ResourceNotFoundException("Camp introuvable") }

    private fun findUser(userId: UUID) =
        userRepository.findById(userId).orElseThrow { ResourceNotFoundException("Utilisateur introuvable") }

    private fun requireClubAdmin(clubId: UUID?, userId: UUID) {
        val adhesion = clubId?.let { adhesionClubRepository.findByUtilisateur_IdAndClub_Id(userId, it) }
        if (adhesion?.role != ROLE_ADMIN || adhesion.statut != STATUT_ACCEPTEE) {
            throw ForbiddenActionException("Seul un administrateur du club peut faire cette action")
        }
    }

    /** Accès à [EffectifEngageEntity.utilisateur]/[EffectifEngageEntity.camp] (relations LAZY) :
     * tous les appelants (addMember, listEffectif, transferPlayer) sont @Transactional. */
    private fun EffectifEngageEntity.toDto(): EffectifDto = EffectifDto(
        id = id.toString(), campId = camp.id.toString(), utilisateurId = utilisateur.id.toString(),
        pseudo = utilisateur.pseudo, numeroMaillot = numeroMaillot, dateEntree = dateEntree.toString(),
        dateSortie = dateSortie?.toString(),
    )
}
