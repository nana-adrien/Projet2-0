package empire.digiprem.projet2_0.server.user.infra.repository

import empire.digiprem.projet2_0.server.user.infra.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {
    fun findByUser_IdAndHashedToken(userId: UUID, hashedToken: String): RefreshTokenEntity?
    fun deleteByUser_IdAndHashedToken(userId: UUID, hashedToken: String)
    fun deleteAllByUser_Id(userId: UUID)
}
