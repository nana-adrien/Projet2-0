package empire.digiprem.projet2_0.server.calendrier.infra.repository

import empire.digiprem.projet2_0.server.calendrier.infra.entity.ConvocationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ConvocationRepository : JpaRepository<ConvocationEntity, UUID> {
    fun findAllByMatch_Id(matchId: UUID): List<ConvocationEntity>
    fun findByMatch_IdAndUtilisateur_Id(matchId: UUID, utilisateurId: UUID): ConvocationEntity?
}
