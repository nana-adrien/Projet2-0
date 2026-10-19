package empire.digiprem.projet2_0.server.user.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

/**
 * RG-01 : une personne = un seul compte, identifié par téléphone ou e-mail (au moins un des deux).
 * RG-20 : suppression -> anonymisation ([supprime] = true), l'historique des matchs n'est jamais effacé.
 */
@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, unique = true, columnDefinition = "text")
    val pseudo: String,

    @Column(unique = true, columnDefinition = "text")
    val telephone: String? = null,

    @Column(unique = true, columnDefinition = "text")
    val email: String? = null,

    @Column(columnDefinition = "text")
    val hashedPassword: String? = null,

    @Column(columnDefinition = "text")
    val nom: String? = null,

    @Column(columnDefinition = "text")
    val prenom: String? = null,

    @Column(columnDefinition = "text")
    val photo: String? = null,

    val dateNaissance: LocalDate? = null,

    @Column(columnDefinition = "text")
    val postePrefere: String? = null,

    @Column(columnDefinition = "text")
    val piedFort: String? = null,

    val cacherNomReel: Boolean = false,

    val cacherAge: Boolean = false,

    val isVerified: Boolean = false,

    val supprime: Boolean = false,

    val createdAt: Instant = Instant.now(),
)
