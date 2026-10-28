package empire.digiprem.projet2_0.server.user.infra.repository

import empire.digiprem.projet2_0.server.user.infra.entity.UserDeviceConfigEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.UUID

interface UserDeviceConfigRepository: JpaRepository<UserDeviceConfigEntity, UUID> {
     fun findAllByUserId(userId: UUID): List<UserDeviceConfigEntity>

     // Trouve un appareil par son identifiant matériel unique
     fun findByDeviceId(deviceId: String): UserDeviceConfigEntity?

     // Liste tous les appareils d'un joueur, du PLUS ANCIEN au PLUS RÉCENT (Tri ascendant)
     fun findAllByUserIdOrderByLastUsedAtAsc(userId: UUID): List<UserDeviceConfigEntity>



     fun deleteAllByUserId(userId: UUID)

     /**
      * 2. ÉVITER L'ACCUMULATION SUR UN MÊME APPAREIL
      * Recherche si ce joueur possède déjà précisément ce jeton sur son téléphone actuel.
      */
     fun findByUserIdAndToken(userId: UUID, token: String): UserDeviceConfigEntity?
     @Modifying
     @Query("DELETE FROM UserDeviceConfigEntity t WHERE t.token = :token")
     fun deleteByToken(@Param("token") token: String)
     /**
      * 4. GESTION DU LOGOUT / DÉCONNEXION
      * Supprime un jeton spécifique lorsqu'un joueur se déconnecte de son appareil.
      */
     @Modifying
     @Query("DELETE FROM UserDeviceConfigEntity t WHERE t.user.id = :userId AND t.token = :token")
     fun deleteByUserIdAndToken(@Param("userId") userId: UUID, @Param("token") token: String)

     @Modifying
     @Query("DELETE FROM UserDeviceConfigEntity t WHERE t.user.id = :userId AND t.lastUsedAt < :threshold")
     fun deleteOldTokens(@Param("userId") userId: UUID, @Param("threshold") threshold: Instant)

     // Déconnexion ciblée à distance
     fun deleteByUserIdAndDeviceId(userId: UUID, deviceId: String)

}