package empire.digiprem.projet2_0.server.calendrier.infra.repository

import empire.digiprem.projet2_0.server.calendrier.infra.entity.CompositionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CompositionRepository : JpaRepository<CompositionEntity, UUID> {
    fun findAllByMatchId(matchId: UUID): List<CompositionEntity>
    fun findByMatchIdAndUtilisateurId(matchId: UUID, utilisateurId: UUID): CompositionEntity?
}
