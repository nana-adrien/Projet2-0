package empire.digiprem.projet2_0.server.deuxzero.infra.repository

import empire.digiprem.projet2_0.server.deuxzero.infra.entity.EffectifEngageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EffectifEngageRepository : JpaRepository<EffectifEngageEntity, UUID> {
    fun findAllByCampId(campId: UUID): List<EffectifEngageEntity>
    fun findByCampIdAndUtilisateurIdAndDateSortieIsNull(campId: UUID, utilisateurId: UUID): EffectifEngageEntity?
    fun findAllByUtilisateurIdAndDateSortieIsNull(utilisateurId: UUID): List<EffectifEngageEntity>
}
