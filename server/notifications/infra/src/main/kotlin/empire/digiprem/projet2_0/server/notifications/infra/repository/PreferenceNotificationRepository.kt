package empire.digiprem.projet2_0.server.notifications.infra.repository

import empire.digiprem.projet2_0.server.notifications.infra.entity.PreferenceNotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PreferenceNotificationRepository : JpaRepository<PreferenceNotificationEntity, UUID> {
    fun findByUtilisateur_IdAndDeuxZero_Id(utilisateurId: UUID, deuxZeroId: UUID): PreferenceNotificationEntity?
}
