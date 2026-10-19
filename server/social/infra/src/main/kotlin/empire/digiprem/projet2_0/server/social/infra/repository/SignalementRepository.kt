package empire.digiprem.projet2_0.server.social.infra.repository

import empire.digiprem.projet2_0.server.social.infra.entity.SignalementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SignalementRepository : JpaRepository<SignalementEntity, UUID>
