package empire.digiprem.projet2_0.server.calendrier.infra.repository

import empire.digiprem.projet2_0.server.calendrier.infra.entity.ValidationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ValidationRepository : JpaRepository<ValidationEntity, UUID> {
    fun findAllByMatch_Id(matchId: UUID): List<ValidationEntity>
    fun findByMatch_IdAndCamp_Id(matchId: UUID, campId: UUID): ValidationEntity?
}
