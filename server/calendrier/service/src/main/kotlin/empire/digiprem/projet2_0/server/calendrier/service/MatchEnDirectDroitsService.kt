package empire.digiprem.projet2_0.server.calendrier.service

import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.CampRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.EffectifEngageRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * Droits spécifiques à la composition d'équipe (CU16) et à la saisie en direct (CU17) —
 * distincts de [CalendrierDroitsService] : ici le capitaine n'agit que sur SON camp, et seul le
 * marqueur désigné (ou son suppléant) peut saisir des événements (RG-11).
 */
@Service
class MatchEnDirectDroitsService(
    private val campRepository: CampRepository,
    private val effectifEngageRepository: EffectifEngageRepository,
) {

    /** Résout le camp (du 2-0 donné) auquel appartient [utilisateurId] via son effectif engagé actif. */
    @Transactional
    fun campDuJoueur(deuxZeroId: UUID, utilisateurId: UUID): UUID? =
        campRepository.findAllByDeuxZero_Id(deuxZeroId).firstOrNull { camp ->
            val campId = camp.id ?: return@firstOrNull false
            effectifEngageRepository.findAllByCamp_Id(campId).any { it.utilisateur.id == utilisateurId && it.dateSortie == null }
        }?.id

    /** @Transactional : accès aux relations LAZY CampEntity.capitaine/viceCapitaine. */
    @Transactional
    fun requireCapitaineOuViceCapitaineDuCamp(campId: UUID, requesterId: UUID) {
        val camp = campRepository.findById(campId).orElseThrow { ResourceNotFoundException("Camp introuvable") }
        if (camp.capitaine?.id != requesterId && camp.viceCapitaine?.id != requesterId) {
            throw ForbiddenActionException("Seul le capitaine ou le vice-capitaine de ce camp peut faire cette action")
        }
    }

    /** RG-11 — seul le marqueur désigné ou son suppléant peut saisir des événements.
     * @Transactional : accès aux relations LAZY MatchEntity.marqueur/marqueurSuppleant. */
    @Transactional
    fun requireMarqueurOuSuppleant(match: MatchEntity, requesterId: UUID) {
        if (match.marqueur?.id != requesterId && match.marqueurSuppleant?.id != requesterId) {
            throw ForbiddenActionException("Seul le marqueur désigné ou son suppléant peut saisir des événements")
        }
    }
}
