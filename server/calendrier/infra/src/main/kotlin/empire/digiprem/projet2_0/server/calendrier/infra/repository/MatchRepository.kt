package empire.digiprem.projet2_0.server.calendrier.infra.repository

import empire.digiprem.projet2_0.server.calendrier.infra.entity.MatchEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MatchRepository : JpaRepository<MatchEntity, UUID> {
    fun findAllBySaison_Id(saisonId: UUID): List<MatchEntity>
    fun findAllBySaison_IdAndStatut(saisonId: UUID, statut: String): List<MatchEntity>
}
