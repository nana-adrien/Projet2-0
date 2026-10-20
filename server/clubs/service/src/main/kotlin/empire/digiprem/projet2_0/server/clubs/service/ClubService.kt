package empire.digiprem.projet2_0.server.clubs.service

import empire.digiprem.projet2_0.contracts.club.ClubDto
import empire.digiprem.projet2_0.contracts.club.CreateClubRequest
import empire.digiprem.projet2_0.contracts.club.JoinClubRequest
import empire.digiprem.projet2_0.contracts.club.MembershipDto
import empire.digiprem.projet2_0.contracts.club.UpdateClubSettingsRequest
import empire.digiprem.projet2_0.contracts.validation.validateCreateClubRequest
import empire.digiprem.projet2_0.contracts.validation.validateJoinClubRequest
import empire.digiprem.projet2_0.server.clubs.infra.entity.AdhesionClubEntity
import empire.digiprem.projet2_0.server.clubs.infra.entity.ClubEntity
import empire.digiprem.projet2_0.server.clubs.infra.repository.AdhesionClubRepository
import empire.digiprem.projet2_0.server.clubs.infra.repository.ClubRepository
import empire.digiprem.projet2_0.server.common.exception.ConflictException
import empire.digiprem.projet2_0.server.common.exception.ForbiddenActionException
import empire.digiprem.projet2_0.server.common.exception.ResourceNotFoundException
import empire.digiprem.projet2_0.server.common.util.orThrow
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

private const val ROLE_ADMIN = "ADMINISTRATEUR"
private const val ROLE_MEMBRE = "MEMBRE"
private const val STATUT_EN_ATTENTE = "EN_ATTENTE"
private const val STATUT_ACCEPTEE = "ACCEPTEE"
private const val STATUT_REFUSEE = "REFUSEE"
private const val STATUT_QUITTEE = "QUITTEE"

/** CU5 (créer), CU6 (rejoindre), CU7 (gérer les membres). */
@Service
class ClubService(
    private val clubRepository: ClubRepository,
    private val adhesionClubRepository: AdhesionClubRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun createClub(userId: UUID, request: CreateClubRequest): ClubDto {
        validateCreateClubRequest(request).orThrow()
        val user = findUser(userId)

        var code = ClubCodeGenerator.generate(request.nom)
        while (clubRepository.findByCodeAdhesion(code) != null) {
            code = ClubCodeGenerator.generate(request.nom)
        }

        val club = clubRepository.save(
            ClubEntity(
                nom = request.nom.trim(),
                logo = request.logo,
                couleurs = request.couleurs,
                quartier = request.quartier,
                description = request.description,
                codeAdhesion = code,
            )
        )

        adhesionClubRepository.save(
            AdhesionClubEntity(
                utilisateur = user,
                club = club,
                role = ROLE_ADMIN,
                statut = STATUT_ACCEPTEE,
                dateReponse = Instant.now(),
            )
        )
        return club.toDto()
    }

    /** CU6 — y compris l'appel déclenché depuis l'inscription (CU1 étape 4) via [ClubEventListener]. */
    @Transactional
    fun requestJoin(userId: UUID, request: JoinClubRequest): MembershipDto {
        validateJoinClubRequest(request).orThrow()
        val club = clubRepository.findByCodeAdhesion(request.code.trim())
            ?: throw ResourceNotFoundException("Ce code ne correspond à aucun club")
        return joinClub(userId, club)
    }

    @Transactional
    fun requestJoinByCode(userId: UUID, code: String) {
        val club = clubRepository.findByCodeAdhesion(code.trim()) ?: return
        joinClub(userId, club)
    }

    private fun joinClub(userId: UUID, club: ClubEntity): MembershipDto {
        val clubId = club.id ?: throw ResourceNotFoundException("Club introuvable")
        val user = findUser(userId)
        val existing = adhesionClubRepository.findByUtilisateur_IdAndClub_Id(userId, clubId)
        if (existing != null && existing.statut in setOf(STATUT_EN_ATTENTE, STATUT_ACCEPTEE)) {
            throw ConflictException("Tu es déjà membre de ce club ou ta demande est en attente")
        }

        val statut = if (club.acceptationAutomatique) STATUT_ACCEPTEE else STATUT_EN_ATTENTE
        val adhesion = adhesionClubRepository.save(
            AdhesionClubEntity(
                id = existing?.id,
                utilisateur = user,
                club = club,
                role = ROLE_MEMBRE,
                statut = statut,
                dateReponse = if (statut == STATUT_ACCEPTEE) Instant.now() else null,
            )
        )
        return adhesion.toDto(club)
    }

    /** N'importe quel membre accepté (quel que soit son rôle) peut consulter la fiche du club. */
    fun getClub(clubId: UUID, requesterId: UUID): ClubDto {
        val adhesion = adhesionClubRepository.findByUtilisateur_IdAndClub_Id(requesterId, clubId)
        if (adhesion?.statut != STATUT_ACCEPTEE) {
            throw ForbiddenActionException("Il faut être membre accepté de ce club")
        }
        return findClub(clubId).toDto()
    }

    /** @Transactional : [AdhesionClubEntity.club] et [AdhesionClubEntity.utilisateur]
     * (via [toDto]) sont des relations LAZY désormais navigées directement — sans la transaction,
     * l'accès déclenche une LazyInitializationException après la fermeture de la session. */
    @Transactional
    fun listMyClubs(userId: UUID): List<MembershipDto> =
        adhesionClubRepository.findAllByUtilisateur_Id(userId).map { adhesion -> adhesion.toDto(adhesion.club) }

    @Transactional
    fun listMembers(clubId: UUID, requesterId: UUID): List<MembershipDto> {
        requireAdmin(clubId, requesterId)
        val club = findClub(clubId)
        return adhesionClubRepository.findAllByClub_Id(clubId).map { it.toDto(club) }
    }

    @Transactional
    fun respondToRequest(clubId: UUID, requesterId: UUID, targetUserId: UUID, accept: Boolean): MembershipDto {
        requireAdmin(clubId, requesterId)
        val adhesion = adhesionClubRepository.findByUtilisateur_IdAndClub_Id(targetUserId, clubId)
            ?: throw ResourceNotFoundException("Demande introuvable")
        val updated = adhesionClubRepository.save(
            AdhesionClubEntity(
                id = adhesion.id,
                utilisateur = adhesion.utilisateur,
                club = adhesion.club,
                role = adhesion.role,
                statut = if (accept) STATUT_ACCEPTEE else STATUT_REFUSEE,
                dateDemande = adhesion.dateDemande,
                dateReponse = Instant.now(),
            )
        )
        return updated.toDto(findClub(clubId))
    }

    @Transactional
    fun regenerateCode(clubId: UUID, requesterId: UUID): ClubDto {
        requireAdmin(clubId, requesterId)
        val club = findClub(clubId)
        var code = ClubCodeGenerator.generate(club.nom)
        while (clubRepository.findByCodeAdhesion(code) != null) {
            code = ClubCodeGenerator.generate(club.nom)
        }
        return club.copyWith(codeAdhesion = code).let(clubRepository::save).toDto()
    }

    @Transactional
    fun updateSettings(clubId: UUID, requesterId: UUID, request: UpdateClubSettingsRequest): ClubDto {
        requireAdmin(clubId, requesterId)
        val club = findClub(clubId)
        return club.copyWith(
            acceptationAutomatique = request.acceptationAutomatique ?: club.acceptationAutomatique,
            delegationCapitaines = request.delegationCapitaines ?: club.delegationCapitaines,
        ).let(clubRepository::save).toDto()
    }

    /** F-CLB-05 : ajoute un co-administrateur sans retirer les droits du demandeur. */
    @Transactional
    fun nameCoAdmin(clubId: UUID, requesterId: UUID, targetUserId: UUID): MembershipDto {
        requireAdmin(clubId, requesterId)
        return setRole(clubId, targetUserId, ROLE_ADMIN)
    }

    /** F-CLB-06 : transfère le rôle, le demandeur redevient simple membre. */
    @Transactional
    fun transferAdminRole(clubId: UUID, requesterId: UUID, targetUserId: UUID): MembershipDto {
        requireAdmin(clubId, requesterId)
        val result = setRole(clubId, targetUserId, ROLE_ADMIN)
        setRole(clubId, requesterId, ROLE_MEMBRE)
        return result
    }

    /** F-CLB-07 : un membre quitte de lui-même. E1 : dernier administrateur sans successeur -> bloqué. */
    @Transactional
    fun leaveClub(userId: UUID, clubId: UUID) {
        val adhesion = adhesionClubRepository.findByUtilisateur_IdAndClub_Id(userId, clubId)
            ?: throw ResourceNotFoundException("Adhésion introuvable")
        if (adhesion.role == ROLE_ADMIN) {
            val otherAdmins = adhesionClubRepository.countByClub_IdAndRoleAndStatut(clubId, ROLE_ADMIN, STATUT_ACCEPTEE)
            if (otherAdmins <= 1) {
                throw ForbiddenActionException("Nomme un autre administrateur avant de quitter le club")
            }
        }
        setStatut(adhesion, STATUT_QUITTEE)
    }

    /** CU7 — l'administrateur retire un membre. Même garde-fou que [leaveClub]. */
    @Transactional
    fun removeMember(clubId: UUID, requesterId: UUID, targetUserId: UUID) {
        requireAdmin(clubId, requesterId)
        val adhesion = adhesionClubRepository.findByUtilisateur_IdAndClub_Id(targetUserId, clubId)
            ?: throw ResourceNotFoundException("Adhésion introuvable")
        if (adhesion.role == ROLE_ADMIN) {
            val otherAdmins = adhesionClubRepository.countByClub_IdAndRoleAndStatut(clubId, ROLE_ADMIN, STATUT_ACCEPTEE)
            if (otherAdmins <= 1) {
                throw ForbiddenActionException("Nomme un autre administrateur avant de retirer celui-ci")
            }
        }
        setStatut(adhesion, STATUT_QUITTEE)
    }

    private fun setRole(clubId: UUID, userId: UUID, role: String): MembershipDto {
        val adhesion = adhesionClubRepository.findByUtilisateur_IdAndClub_Id(userId, clubId)
            ?: throw ResourceNotFoundException("Adhésion introuvable")
        val updated = adhesionClubRepository.save(
            AdhesionClubEntity(
                id = adhesion.id,
                utilisateur = adhesion.utilisateur,
                club = adhesion.club,
                role = role,
                statut = adhesion.statut,
                dateDemande = adhesion.dateDemande,
                dateReponse = adhesion.dateReponse,
            )
        )
        return updated.toDto(findClub(clubId))
    }

    private fun setStatut(adhesion: AdhesionClubEntity, statut: String) {
        adhesionClubRepository.save(
            AdhesionClubEntity(
                id = adhesion.id,
                utilisateur = adhesion.utilisateur,
                club = adhesion.club,
                role = adhesion.role,
                statut = statut,
                dateDemande = adhesion.dateDemande,
                dateReponse = Instant.now(),
            )
        )
    }

    private fun requireAdmin(clubId: UUID, userId: UUID) {
        val adhesion = adhesionClubRepository.findByUtilisateur_IdAndClub_Id(userId, clubId)
        if (adhesion?.role != ROLE_ADMIN || adhesion.statut != STATUT_ACCEPTEE) {
            throw ForbiddenActionException("Seul un administrateur du club peut faire cette action")
        }
    }

    private fun findClub(clubId: UUID): ClubEntity =
        clubRepository.findById(clubId).orElseThrow { ResourceNotFoundException("Club introuvable") }

    private fun findUser(userId: UUID) =
        userRepository.findById(userId).orElseThrow { ResourceNotFoundException("Utilisateur introuvable") }

    private fun ClubEntity.copyWith(
        codeAdhesion: String = this.codeAdhesion,
        acceptationAutomatique: Boolean = this.acceptationAutomatique,
        delegationCapitaines: Boolean = this.delegationCapitaines,
    ) = ClubEntity(
        id = id,
        nom = nom,
        logo = logo,
        couleurs = couleurs,
        quartier = quartier,
        description = description,
        codeAdhesion = codeAdhesion,
        statut = statut,
        acceptationAutomatique = acceptationAutomatique,
        delegationCapitaines = delegationCapitaines,
    )

    private fun ClubEntity.toDto() = ClubDto(
        id = id.toString(),
        nom = nom,
        logo = logo,
        couleurs = couleurs,
        quartier = quartier,
        description = description,
        codeAdhesion = codeAdhesion,
        statut = statut,
        acceptationAutomatique = acceptationAutomatique,
        delegationCapitaines = delegationCapitaines,
    )

    private fun AdhesionClubEntity.toDto(club: ClubEntity): MembershipDto {
        return MembershipDto(
            id = id.toString(),
            clubId = club.id.toString(),
            clubNom = club.nom,
            clubLogo = club.logo,
            utilisateurId = utilisateur.id.toString(),
            pseudo = utilisateur.pseudo,
            role = role,
            statut = statut,
            dateDemande = dateDemande.toString(),
            dateReponse = dateReponse?.toString(),
        )
    }
}
