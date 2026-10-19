package empire.digiprem.projet2_0.server.deuxzero.infra.repository

import empire.digiprem.projet2_0.server.deuxzero.infra.entity.DeuxZeroEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DeuxZeroRepository : JpaRepository<DeuxZeroEntity, UUID> {
    fun findAllByOrganisateurId(organisateurId: UUID): List<DeuxZeroEntity>
}
