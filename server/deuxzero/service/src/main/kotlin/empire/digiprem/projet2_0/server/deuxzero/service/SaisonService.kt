package empire.digiprem.projet2_0.server.deuxzero.service

import empire.digiprem.projet2_0.contracts.deuxzero.OpenSaisonRequest
import empire.digiprem.projet2_0.contracts.deuxzero.SaisonDto
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.deuxzero.infra.entity.DeuxZeroEntity
import empire.digiprem.projet2_0.server.deuxzero.infra.entity.SaisonEntity
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.DeuxZeroRepository
import empire.digiprem.projet2_0.server.deuxzero.infra.repository.SaisonRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

/** CU12 — Ouvrir / clôturer une saison. */
@Service
class SaisonService(
    private val saisonRepository: SaisonRepository,
    private val deuxZeroRepository: DeuxZeroRepository,
) {

    /** @Transactional : [SaisonEntity.deuxZero]/[SaisonEntity.campChampion] (relations LAZY)
     * sont navigués directement par [toDto]. */
    @Transactional
    fun listSaisons(deuxZeroId: UUID): List<SaisonDto> =
        saisonRepository.findAllByDeuxZero_Id(deuxZeroId).map { it.toDto() }

    @Transactional
    fun openSaison(deuxZeroId: UUID, requesterId: UUID, request: OpenSaisonRequest): SaisonDto {
        val deuxZero = requireOrganisateur(deuxZeroId, requesterId)
        return saisonRepository.save(
            SaisonEntity(
                deuxZero = deuxZero,
                libelle = request.libelle.trim(),
                dateDebut = LocalDate.parse(request.dateDebut),
                dateFin = request.dateFin?.let { LocalDate.parse(it) },
                statut = "EN_COURS",
            )
        ).toDto()
    }

    /**
     * RG-17 (camp champion) et RG-18 (saison figée) : le calcul du champion dépend des matchs et
     * de leurs événements, pas encore modélisés côté serveur (Jalon 3). On clôture la saison dès
     * maintenant ; le champion sera calculé par le module statistiques au Jalon 5 — [SaisonDto.campChampionId]
     * reste nul jusque-là plutôt que d'inventer un calcul sans données réelles.
     */
    @Transactional
    fun closeSaison(saisonId: UUID, requesterId: UUID): SaisonDto {
        val saison = saisonRepository.findById(saisonId).orElseThrow { ResourceNotFoundException("Saison introuvable") }
        requireOrganisateur(saison.deuxZero.id!!, requesterId)
        if (saison.statut == "CLOTUREE") {
            throw ForbiddenActionException("Cette saison est déjà clôturée")
        }

        return saisonRepository.save(
            SaisonEntity(
                id = saison.id,
                deuxZero = saison.deuxZero,
                libelle = saison.libelle,
                dateDebut = saison.dateDebut,
                dateFin = saison.dateFin ?: LocalDate.now(),
                statut = "CLOTUREE",
                campChampion = saison.campChampion,
            )
        ).toDto()
    }

    private fun requireOrganisateur(deuxZeroId: UUID, userId: UUID): DeuxZeroEntity {
        val deuxZero = deuxZeroRepository.findById(deuxZeroId).orElseThrow { ResourceNotFoundException("2-0 introuvable") }
        if (deuxZero.organisateur.id != userId) {
            throw ForbiddenActionException("Seul l'organisateur du 2-0 peut faire cette action")
        }
        return deuxZero
    }

    private fun SaisonEntity.toDto() = SaisonDto(
        id = id.toString(), deuxZeroId = deuxZero.id.toString(), libelle = libelle,
        dateDebut = dateDebut.toString(), dateFin = dateFin?.toString(), statut = statut,
        campChampionId = campChampion?.id?.toString(),
    )
}
