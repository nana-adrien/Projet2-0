package empire.digiprem.projet2_0.server.deuxzero.infra.repository

import empire.digiprem.projet2_0.server.deuxzero.infra.entity.EffectifEngageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EffectifEngageRepository : JpaRepository<EffectifEngageEntity, UUID> {
    fun findAllByCamp_Id(campId: UUID): List<EffectifEngageEntity>
    fun findByCamp_IdAndUtilisateur_IdAndDateSortieIsNull(campId: UUID, utilisateurId: UUID): EffectifEngageEntity?
    fun findAllByUtilisateur_IdAndDateSortieIsNull(utilisateurId: UUID): List<EffectifEngageEntity>
}
