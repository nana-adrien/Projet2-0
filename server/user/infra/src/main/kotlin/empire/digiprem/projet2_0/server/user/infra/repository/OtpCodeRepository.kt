package empire.digiprem.projet2_0.server.user.infra.repository

import empire.digiprem.projet2_0.server.user.infra.entity.OtpCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OtpCodeRepository : JpaRepository<OtpCodeEntity, UUID> {
    fun findByContactAndPurpose(contact: String, purpose: String): OtpCodeEntity?
    fun deleteByContactAndPurpose(contact: String, purpose: String)
}
