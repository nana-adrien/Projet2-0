package empire.digiprem.projet2_0.server.clubs.infra.repository

import empire.digiprem.projet2_0.server.clubs.infra.entity.AdhesionClubEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AdhesionClubRepository : JpaRepository<AdhesionClubEntity, UUID> {
    fun findByUtilisateur_IdAndClub_Id(utilisateurId: UUID, clubId: UUID): AdhesionClubEntity?
    fun findAllByClub_Id(clubId: UUID): List<AdhesionClubEntity>
    fun findAllByUtilisateur_Id(utilisateurId: UUID): List<AdhesionClubEntity>
    fun countByClub_IdAndRoleAndStatut(clubId: UUID, role: String, statut: String): Long
}
