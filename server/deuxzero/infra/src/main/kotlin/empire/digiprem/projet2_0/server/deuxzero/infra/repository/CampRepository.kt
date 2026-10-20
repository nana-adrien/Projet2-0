package empire.digiprem.projet2_0.server.deuxzero.infra.repository

import empire.digiprem.projet2_0.server.deuxzero.infra.entity.CampEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CampRepository : JpaRepository<CampEntity, UUID> {
    fun findByCodeEngagement(codeEngagement: String): CampEntity?
    fun findAllByDeuxZero_Id(deuxZeroId: UUID): List<CampEntity>
    fun findAllByCapitaine_IdOrViceCapitaine_Id(capitaineId: UUID, viceCapitaineId: UUID): List<CampEntity>
}
