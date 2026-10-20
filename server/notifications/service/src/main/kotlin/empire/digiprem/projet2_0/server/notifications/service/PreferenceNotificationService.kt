package empire.digiprem.projet2_0.server.notifications.service

import empire.digiprem.projet2_0.contracts.notifications.PreferenceNotificationDto
import empire.digiprem.projet2_0.contracts.notifications.SetPreferenceNotificationRequest
import empire.digiprem.projet2_0.contracts.validation.validateSetPreferenceNotificationRequest
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.common.util.orThrow
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.CampRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.DeuxZeroRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.EffectifEngageRepository
import empire.digiprem.projet2_0.server.notifications.infra.entity.PreferenceNotificationEntity
import empire.digiprem.projet2_0.server.notifications.infra.repository.PreferenceNotificationRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PreferenceNotificationService(
    private val preferenceRepository: PreferenceNotificationRepository,
    private val deuxZeroRepository: DeuxZeroRepository,
    private val campRepository: CampRepository,
    private val effectifEngageRepository: EffectifEngageRepository,
    private val userRepository: UserRepository,
) {

    /** @Transactional : accès aux relations LAZY PreferenceNotificationEntity.deuxZero (via
     * toDto) et DeuxZeroEntity.organisateur (via requireMembreDuDeuxZero). */
    @Transactional
    fun getPreference(deuxZeroId: UUID, requesterId: UUID): PreferenceNotificationDto {
        requireMembreDuDeuxZero(deuxZeroId, requesterId)
        val existing = preferenceRepository.findByUtilisateur_IdAndDeuxZero_Id(requesterId, deuxZeroId)
        val preference = existing ?: preferenceRepository.save(
            PreferenceNotificationEntity(
                utilisateur = userRepository.getReferenceById(requesterId),
                deuxZero = deuxZeroRepository.getReferenceById(deuxZeroId),
            )
        )
        return preference.toDto()
    }

    @Transactional
    fun setPreference(deuxZeroId: UUID, requesterId: UUID, request: SetPreferenceNotificationRequest): PreferenceNotificationDto {
        validateSetPreferenceNotificationRequest(request).orThrow()
        requireMembreDuDeuxZero(deuxZeroId, requesterId)
        val existing = preferenceRepository.findByUtilisateur_IdAndDeuxZero_Id(requesterId, deuxZeroId)
        val updated = preferenceRepository.save(
            PreferenceNotificationEntity(
                id = existing?.id,
                utilisateur = userRepository.getReferenceById(requesterId),
                deuxZero = deuxZeroRepository.getReferenceById(deuxZeroId),
                niveau = request.niveau,
                neRienDerangerNocturne = request.neRienDerangerNocturne,
            )
        )
        return updated.toDto()
    }

    private fun requireMembreDuDeuxZero(deuxZeroId: UUID, requesterId: UUID) {
        val deuxZero = deuxZeroRepository.findById(deuxZeroId).orElseThrow { ResourceNotFoundException("2-0 introuvable") }
        if (deuxZero.organisateur.id == requesterId) return
        val campsEngages = effectifEngageRepository.findAllByUtilisateur_IdAndDateSortieIsNull(requesterId)
            .mapNotNull { it.camp.id }
            .toSet()
        val estMembre = campRepository.findAllByDeuxZero_Id(deuxZeroId).any { it.id in campsEngages }
        if (!estMembre) {
            throw ForbiddenActionException("Il faut etre membre de ce 2-0 pour gerer ses preferences de notification")
        }
    }

    /** Accès à la relation LAZY PreferenceNotificationEntity.deuxZero : appelants (getPreference,
     * setPreference) déjà @Transactional. */
    private fun PreferenceNotificationEntity.toDto() = PreferenceNotificationDto(
        id = id.toString(),
        deuxZeroId = deuxZero.id.toString(),
        niveau = niveau,
        neRienDerangerNocturne = neRienDerangerNocturne,
    )
}
