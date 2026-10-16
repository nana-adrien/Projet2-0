package empire.digiprem.projet2_0.server.calendrier.service

import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.DeuxZeroRepository
import org.springframework.stereotype.Service
import java.util.UUID

/** Droit d'arbitrage d'un litige (CU20) : réservé à l'organisateur du 2-0. Les capitaines et
 * vice-capitaines des deux camps en sont explicitement exclus (conflit d'intérêt, décision déjà
 * tracée dans uml/tracabilite.json CU20) — même s'ils sont par ailleurs "gestionnaire du
 * calendrier" au sens de [CalendrierDroitsService]. */
@Service
class MatchArbitrageDroitsService(
    private val deuxZeroRepository: DeuxZeroRepository,
) {

    fun requireOrganisateur(deuxZeroId: UUID, requesterId: UUID) {
        val deuxZero = deuxZeroRepository.findById(deuxZeroId).orElseThrow { ResourceNotFoundException("2-0 introuvable") }
        if (deuxZero.organisateurId != requesterId) {
            throw ForbiddenActionException("Seul l'organisateur du 2-0 peut arbitrer un litige")
        }
    }
}
