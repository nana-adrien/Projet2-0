package empire.digiprem.projet2_0.server.user.infra.repository

import empire.digiprem.projet2_0.server.user.infra.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByPseudo(pseudo: String): UserEntity?
    fun findByTelephoneOrEmail(telephone: String, email: String): UserEntity?
    fun findByPseudoOrTelephoneOrEmail(pseudo: String, telephone: String, email: String): UserEntity?

    fun findByTelephone(telephone: String): UserEntity?
    fun findByEmail(email: String): UserEntity?
}
