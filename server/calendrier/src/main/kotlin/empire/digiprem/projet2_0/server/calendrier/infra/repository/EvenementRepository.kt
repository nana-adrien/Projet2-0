package empire.digiprem.projet2_0.server.calendrier.infra.repository

import empire.digiprem.projet2_0.server.calendrier.infra.entity.EvenementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EvenementRepository : JpaRepository<EvenementEntity, UUID> {
    fun findAllByMatchIdOrderByPeriodeAscMinuteAsc(matchId: UUID): List<EvenementEntity>
    fun findByClientEventId(clientEventId: UUID): EvenementEntity?
    fun findFirstByMatchIdAndAnnuleFalseOrderByHorodatageDesc(matchId: UUID): EvenementEntity?
}
