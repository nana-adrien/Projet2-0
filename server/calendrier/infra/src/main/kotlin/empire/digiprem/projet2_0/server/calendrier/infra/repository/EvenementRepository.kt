package empire.digiprem.projet2_0.server.calendrier.infra.repository

import empire.digiprem.projet2_0.server.calendrier.infra.entity.EvenementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EvenementRepository : JpaRepository<EvenementEntity, UUID> {
    fun findAllByMatch_IdOrderByPeriodeAscMinuteAsc(matchId: UUID): List<EvenementEntity>
    fun findByClientEventId(clientEventId: UUID): EvenementEntity?
    fun findFirstByMatch_IdAndAnnuleFalseOrderByHorodatageDesc(matchId: UUID): EvenementEntity?
}
