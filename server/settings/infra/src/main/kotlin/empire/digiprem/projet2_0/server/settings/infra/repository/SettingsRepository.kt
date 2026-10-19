package empire.digiprem.projet2_0.server.settings.infra.repository

import empire.digiprem.projet2_0.server.settings.infra.entity.SettingsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SettingsRepository : JpaRepository<SettingsEntity, UUID>
