package empire.digiprem.projet2_0.server.calendrier.service

import empire.digiprem.projet2_0.server.deuxzero.infra.repository.CampRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

/** Droit de signature de la feuille de match (CU19) : seul un capitaine ou un vice-capitaine
 * d'un des deux camps peut signer, et uniquement pour SON camp — le mobile n'envoie jamais de
 * campId dans les requêtes valider/contester/repondre-contestation, c'est le serveur qui le
 * résout à partir du requester (cf. ValidationService). */
@Service
class ValidationDroitsService(
    private val campRepository: CampRepository,
) {

    /** Résout le camp (du 2-0 donné) dont [requesterId] est capitaine ou vice-capitaine, ou
     * `null` si [requesterId] n'occupe ce rôle dans aucun des deux camps.
     * @Transactional : accès aux relations LAZY CampEntity.capitaine/viceCapitaine. */
    @Transactional
    fun campDuCapitaineOuViceCapitaine(deuxZeroId: UUID, requesterId: UUID): UUID? =
        campRepository.findAllByDeuxZero_Id(deuxZeroId)
            .firstOrNull { it.capitaine?.id == requesterId || it.viceCapitaine?.id == requesterId }?.id
}
