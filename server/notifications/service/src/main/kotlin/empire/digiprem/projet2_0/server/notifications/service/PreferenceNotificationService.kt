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
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PreferenceNotificationService(
    private val preferenceRepository: PreferenceNotificationRepository,
    private val deuxZeroRepository: DeuxZeroRepository,
    private val campRepository: CampRepository,
    private val effectifEngageRepository: EffectifEngageRepository,
) {

    fun getPreference(deuxZeroId: UUID, requesterId: UUID): PreferenceNotificationDto {
        requireMembreDuDeuxZero(deuxZeroId, requesterId)
        val existing = preferenceRepository.findByUtilisateurIdAndDeuxZeroId(requesterId, deuxZeroId)
        val preference = existing ?: preferenceRepository.save(
            PreferenceNotificationEntity(utilisateurId = requesterId, deuxZeroId = deuxZeroId)
        )
        return preference.toDto()
    }

    @Transactional
    fun setPreference(deuxZeroId: UUID, requesterId: UUID, request: SetPreferenceNotificationRequest): PreferenceNotificationDto {
        validateSetPreferenceNotificationRequest(request).orThrow()
        requireMembreDuDeuxZero(deuxZeroId, requesterId)
        val existing = preferenceRepository.findByUtilisateurIdAndDeuxZeroId(requesterId, deuxZeroId)
        val updated = preferenceRepository.save(
            PreferenceNotificationEntity(
                id = existing?.id,
                utilisateurId = requesterId,
                deuxZeroId = deuxZeroId,
                niveau = request.niveau,
                neRienDerangerNocturne = request.neRienDerangerNocturne,
            )
        )
        return updated.toDto()
    }

    private fun requireMembreDuDeuxZero(deuxZeroId: UUID, requesterId: UUID) {
        val deuxZero = deuxZeroRepository.findById(deuxZeroId).orElseThrow { ResourceNotFoundException("2-0 introuvable") }
        if (deuxZero.organisateurId == requesterId) return
        val campsEngages = effectifEngageRepository.findAllByUtilisateurIdAndDateSortieIsNull(requesterId)
            .map { it.campId }
            .toSet()
        val estMembre = campRepository.findAllByDeuxZeroId(deuxZeroId).any { it.id in campsEngages }
        if (!estMembre) {
            throw ForbiddenActionException("Il faut etre membre de ce 2-0 pour gerer ses preferences de notification")
        }
    }

    private fun PreferenceNotificationEntity.toDto() = PreferenceNotificationDto(
        id = id.toString(),
        deuxZeroId = deuxZeroId.toString(),
        niveau = niveau,
        neRienDerangerNocturne = neRienDerangerNocturne,
    )
}
