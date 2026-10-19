package empire.digiprem.projet2_0.server.statistiques.infra.repository

import empire.digiprem.projet2_0.server.statistiques.infra.entity.VoteEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/** CU24 — accès aux votes "homme du match". */
interface VoteRepository : JpaRepository<VoteEntity, UUID> {
    fun findAllByMatchId(matchId: UUID): List<VoteEntity>

    fun findByMatchIdAndVotantId(matchId: UUID, votantId: UUID): VoteEntity?
}
