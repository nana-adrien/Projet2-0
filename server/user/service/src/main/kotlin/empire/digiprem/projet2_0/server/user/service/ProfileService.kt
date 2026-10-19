package empire.digiprem.projet2_0.server.user.service

import empire.digiprem.projet2_0.contracts.profile.ProfileDto
import empire.digiprem.projet2_0.contracts.profile.UpdateProfileRequest
import empire.digiprem.projet2_0.contracts.validation.validateUpdateProfileRequest
import empire.digiprem.projet2_0.server.common.exception.UserAlreadyExistsException
import empire.digiprem.projet2_0.server.common.exception.UserNotFoundException
import empire.digiprem.projet2_0.server.common.util.orThrow
import empire.digiprem.projet2_0.server.user.infra.entity.UserEntity
import empire.digiprem.projet2_0.server.user.infra.repository.RefreshTokenRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

/** CU4 — Modifier son profil (et supprimer son compte). */
@Service
class ProfileService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    fun getProfile(userId: String): ProfileDto {
        val user = userRepository.findById(UUID.fromString(userId)).orElseThrow { UserNotFoundException() }
        return user.toDto()
    }

    @Transactional
    fun updateProfile(userId: String, request: UpdateProfileRequest): ProfileDto {
        validateUpdateProfileRequest(request).orThrow()
        val uuid = UUID.fromString(userId)
        val existing = userRepository.findById(uuid).orElseThrow { UserNotFoundException() }

        val newPseudo = request.pseudo?.trim()
        if (newPseudo != null && newPseudo != existing.pseudo && userRepository.findByPseudo(newPseudo) != null) {
            throw UserAlreadyExistsException()
        }

        val updated = UserEntity(
            id = existing.id,
            pseudo = newPseudo ?: existing.pseudo,
            telephone = existing.telephone,
            email = existing.email,
            hashedPassword = existing.hashedPassword,
            nom = request.nom ?: existing.nom,
            prenom = request.prenom ?: existing.prenom,
            photo = request.photo ?: existing.photo,
            dateNaissance = request.dateNaissance?.let { java.time.LocalDate.parse(it) } ?: existing.dateNaissance,
            postePrefere = request.postePrefere ?: existing.postePrefere,
            piedFort = request.piedFort ?: existing.piedFort,
            cacherNomReel = request.cacherNomReel ?: existing.cacherNomReel,
            cacherAge = request.cacherAge ?: existing.cacherAge,
            isVerified = existing.isVerified,
            supprime = existing.supprime,
            createdAt = existing.createdAt,
        )
        return userRepository.save(updated).toDto()
    }

    /** RG-20 : suppression -> anonymisation. Le pseudo et l'historique des matchs sont conservés ;
     * la couche de présentation doit afficher "Ancien joueur" dès que [UserEntity.supprime] est vrai. */
    @Transactional
    fun deleteAccount(userId: String) {
        val uuid = UUID.fromString(userId)
        val existing = userRepository.findById(uuid).orElseThrow { UserNotFoundException() }

        userRepository.save(
            UserEntity(
                id = existing.id,
                pseudo = existing.pseudo,
                telephone = null,
                email = null,
                hashedPassword = null,
                nom = null,
                prenom = null,
                photo = null,
                dateNaissance = null,
                postePrefere = existing.postePrefere,
                piedFort = existing.piedFort,
                cacherNomReel = existing.cacherNomReel,
                cacherAge = existing.cacherAge,
                isVerified = existing.isVerified,
                supprime = true,
                createdAt = existing.createdAt,
            )
        )
        refreshTokenRepository.deleteAllByUser_Id(uuid)
    }

    private fun UserEntity.toDto() = ProfileDto(
        userId = id.toString(),
        pseudo = pseudo,
        photo = photo,
        nom = nom,
        prenom = prenom,
        dateNaissance = dateNaissance?.toString(),
        postePrefere = postePrefere,
        piedFort = piedFort,
        cacherNomReel = cacherNomReel,
        cacherAge = cacherAge,
    )
}
