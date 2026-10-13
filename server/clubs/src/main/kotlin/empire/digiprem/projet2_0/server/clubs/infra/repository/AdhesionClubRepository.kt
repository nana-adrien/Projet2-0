package empire.digiprem.projet2_0.server.clubs.infra.repository

import empire.digiprem.projet2_0.server.clubs.infra.entity.AdhesionClubEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AdhesionClubRepository : JpaRepository<AdhesionClubEntity, UUID> {
    fun findByUtilisateurIdAndClubId(utilisateurId: UUID, clubId: UUID): AdhesionClubEntity?
    fun findAllByClubId(clubId: UUID): List<AdhesionClubEntity>
    fun findAllByUtilisateurId(utilisateurId: UUID): List<AdhesionClubEntity>
    fun countByClubIdAndRoleAndStatut(clubId: UUID, role: String, statut: String): Long
}
