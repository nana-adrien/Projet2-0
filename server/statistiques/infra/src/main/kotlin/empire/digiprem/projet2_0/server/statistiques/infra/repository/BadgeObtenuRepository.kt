package empire.digiprem.projet2_0.server.statistiques.infra.repository

import empire.digiprem.projet2_0.server.statistiques.infra.entity.BadgeObtenuEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/** CU25 — accès aux badges obtenus (profil/progression, bilan de saison). */
interface BadgeObtenuRepository : JpaRepository<BadgeObtenuEntity, UUID> {
    fun findAllByUtilisateur_Id(utilisateurId: UUID): List<BadgeObtenuEntity>

    fun existsByUtilisateur_IdAndType(utilisateurId: UUID, type: String): Boolean

    fun countByUtilisateur_IdAndType(utilisateurId: UUID, type: String): Long
}
