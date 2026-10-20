package empire.digiprem.projet2_0.server.calendrier.infra.repository

import empire.digiprem.projet2_0.server.calendrier.infra.entity.CompositionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CompositionRepository : JpaRepository<CompositionEntity, UUID> {
    fun findAllByMatch_Id(matchId: UUID): List<CompositionEntity>
    fun findByMatch_IdAndUtilisateur_Id(matchId: UUID, utilisateurId: UUID): CompositionEntity?
}
