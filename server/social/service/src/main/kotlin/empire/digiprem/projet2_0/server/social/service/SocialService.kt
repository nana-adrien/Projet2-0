package empire.digiprem.projet2_0.server.social.service

import empire.digiprem.projet2_0.contracts.social.SignalementDto
import empire.digiprem.projet2_0.contracts.social.SignalerRequest
import empire.digiprem.projet2_0.contracts.validation.validateSignalerRequest
import empire.digiprem.projet2_0.server.common.util.orThrow
import empire.digiprem.projet2_0.server.social.infra.entity.SignalementEntity
import empire.digiprem.projet2_0.server.social.infra.repository.SignalementRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

private const val STATUT_EN_ATTENTE = "EN_ATTENTE"

/** CU27 -- Signaler un contenu. Scope serveur minimal (F-SOC-05) : creation de l'enregistrement
 * uniquement ; le back-office de moderation (consultation, decision par l'administrateur
 * plateforme) est hors scope mobile ET hors scope de ce jalon serveur. */
@Service
class SocialService(
    private val signalementRepository: SignalementRepository,
) {

    @Transactional
    fun signaler(requesterId: UUID, request: SignalerRequest): SignalementDto {
        validateSignalerRequest(request).orThrow()
        val signalement = signalementRepository.save(
            SignalementEntity(
                auteurId = requesterId,
                motif = request.motif.trim(),
                statut = STATUT_EN_ATTENTE,
                contenuType = request.contenuType,
                contenuId = UUID.fromString(request.contenuId),
            )
        )
        return signalement.toDto()
    }

    private fun SignalementEntity.toDto() = SignalementDto(
        id = id.toString(),
        contenuType = contenuType,
        contenuId = contenuId.toString(),
        motif = motif,
    )
}
