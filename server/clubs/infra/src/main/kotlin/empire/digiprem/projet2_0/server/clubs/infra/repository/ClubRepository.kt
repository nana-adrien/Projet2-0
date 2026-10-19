package empire.digiprem.projet2_0.server.clubs.infra.repository

import empire.digiprem.projet2_0.server.clubs.infra.entity.ClubEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ClubRepository : JpaRepository<ClubEntity, UUID> {
    fun findByCodeAdhesion(codeAdhesion: String): ClubEntity?
}
