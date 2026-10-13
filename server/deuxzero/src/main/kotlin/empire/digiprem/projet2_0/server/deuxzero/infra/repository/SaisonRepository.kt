package empire.digiprem.projet2_0.server.deuxzero.infra.repository

import empire.digiprem.projet2_0.server.deuxzero.infra.entity.SaisonEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SaisonRepository : JpaRepository<SaisonEntity, UUID> {
    fun findAllByDeuxZeroId(deuxZeroId: UUID): List<SaisonEntity>
    fun findFirstByDeuxZeroIdAndStatutOrderByDateDebutDesc(deuxZeroId: UUID, statut: String): SaisonEntity?
}
