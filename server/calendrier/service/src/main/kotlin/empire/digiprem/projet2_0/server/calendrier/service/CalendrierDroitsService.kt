package empire.digiprem.projet2_0.server.calendrier.service

import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.CampRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.DeuxZeroRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.SaisonRepository
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * Droit "gestionnaire du calendrier" (CU14/CU15) : décision Digiprem suite à la fiche KNA — non
 * formalisée dans les RG UML en tant que telle. Un gestionnaire est soit l'organisateur du 2-0,
 * soit le capitaine ou le vice-capitaine d'un des deux camps. Centralisé ici pour être réutilisé
 * par [CalendrierService] et [ConvocationService] sans dupliquer la règle.
 */
@Service
class CalendrierDroitsService(
    private val campRepository: CampRepository,
    private val deuxZeroRepository: DeuxZeroRepository,
    private val saisonRepository: SaisonRepository,
) {

    fun requireGestionnaireCalendrier(deuxZeroId: UUID, requesterId: UUID) {
        val deuxZero = deuxZeroRepository.findById(deuxZeroId).orElseThrow { ResourceNotFoundException("2-0 introuvable") }
        if (deuxZero.organisateurId == requesterId) return

        val estCapitaineOuViceCapitaine = campRepository.findAllByDeuxZeroId(deuxZeroId).any {
            it.capitaineId == requesterId || it.viceCapitaineId == requesterId
        }
        if (!estCapitaineOuViceCapitaine) {
            throw ForbiddenActionException(
                "Seul l'organisateur, un capitaine ou un vice-capitaine du 2-0 peut faire cette action"
            )
        }
    }

    /** Résout le 2-0 porteur de la saison et applique [requireGestionnaireCalendrier]. Retourne le
     * deuxZeroId résolu pour éviter à l'appelant de le recalculer. */
    fun requireGestionnaireCalendrierPourSaison(saisonId: UUID, requesterId: UUID): UUID {
        val saison = saisonRepository.findById(saisonId).orElseThrow { ResourceNotFoundException("Saison introuvable") }
        requireGestionnaireCalendrier(saison.deuxZeroId, requesterId)
        return saison.deuxZeroId
    }
}
