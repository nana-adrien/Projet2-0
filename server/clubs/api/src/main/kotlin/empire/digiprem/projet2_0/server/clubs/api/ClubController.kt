package empire.digiprem.projet2_0.server.clubs.api

import empire.digiprem.projet2_0.contracts.club.CreateClubRequest
import empire.digiprem.projet2_0.contracts.club.JoinClubRequest
import empire.digiprem.projet2_0.contracts.club.UpdateClubSettingsRequest
import empire.digiprem.projet2_0.contracts.base.ApiResponse
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.clubs.service.ClubService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.context.properties.bind.Name
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID



@RestController()
@RequestMapping("/api/v1/clubs", name = "Clubs")
@Tag(name = "Gestion des Clubs", description = "Endpoints pour créer, modifier et rejoindre des clubs")
class ClubController(
    private val clubService: ClubService,
) {

    @Operation(
        summary = "Créer un club",
        description = "Crée un club (nom, logo, couleurs, quartier, description) et nomme automatiquement l'appelant administrateur (F-CLB-01)."
    )
    @PostMapping
    fun createClub(@RequestBody body: CreateClubRequest) =
        clubService.createClub(userId(), body).toApiResponse()

    @Operation(
        summary = "Lister mes clubs",
        description = "Renvoie les clubs dont l'utilisateur connecté est membre."
    )
    @GetMapping("/mine")
    fun listMyClubs() = clubService.listMyClubs(userId()).toApiResponse()

    @Operation(
        summary = "Consulter un club",
        description = "Détail d'un club, réservé à ses membres."
    )
    @GetMapping("/{clubId}")
    fun getClub(
        @Parameter(description = "Identifiant du club") @PathVariable clubId: UUID
    ) = clubService.getClub(clubId, userId()).toApiResponse()


    @Operation(
        summary = "Rejoindre un club",
        description = "Demande d'adhésion via le code d'adhésion ; acceptée immédiatement si l'acceptation automatique est activée, sinon mise en attente de l'administrateur (F-CLB-04)."
    )
    @PostMapping("/join")
    fun joinClub(@RequestBody body: JoinClubRequest) =
        clubService.requestJoin(userId(), body).toApiResponse()

    @Operation(
        summary = "Lister les membres et demandes",
        description = "Membres du club et demandes d'adhésion en attente (F-CLB-05)."
    )
    @GetMapping("/{clubId}/members")
    fun listMembers(
        @Parameter(description = "Identifiant du club") @PathVariable clubId: UUID
    ) = clubService.listMembers(clubId, userId()).toApiResponse()

    @Operation(
        summary = "Accepter une demande d'adhésion",
        description = "Action réservée à l'administrateur du club (F-CLB-04)."
    )
    @PostMapping("/{clubId}/members/{targetUserId}/accept")
    fun acceptMember(
        @Parameter(description = "Identifiant du club") @PathVariable clubId: UUID,
        @Parameter(description = "Identifiant du joueur candidat") @PathVariable targetUserId: UUID,
    ) = clubService.respondToRequest(clubId, userId(), targetUserId, accept = true).toApiResponse()

    @Operation(
        summary = "Refuser une demande d'adhésion",
        description = "Action réservée à l'administrateur du club (F-CLB-04)."
    )
    @PostMapping("/{clubId}/members/{targetUserId}/reject")
    fun rejectMember(
        @Parameter(description = "Identifiant du club") @PathVariable clubId: UUID,
        @Parameter(description = "Identifiant du joueur candidat") @PathVariable targetUserId: UUID,
    ) = clubService.respondToRequest(clubId, userId(), targetUserId, accept = false).toApiResponse()

    @Operation(
        summary = "Retirer un membre",
        description = "Retire un membre du club et des effectifs à venir, tout en conservant son historique (F-CLB-05, F-CLB-07)."
    )
    @DeleteMapping("/{clubId}/members/{targetUserId}")
    fun removeMember(
        @Parameter(description = "Identifiant du club") @PathVariable clubId: UUID,
        @Parameter(description = "Identifiant du membre à retirer") @PathVariable targetUserId: UUID,
    ): ApiResponse {
        clubService.removeMember(clubId, userId(), targetUserId)
        return ApiResponse.ok()
    }

    @Operation(
        summary = "Nommer un co-administrateur",
        description = "L'administrateur délègue ses droits à un membre du club (F-CLB-05)."
    )
    @PostMapping("/{clubId}/members/{targetUserId}/co-admin")
    fun nameCoAdmin(
        @Parameter(description = "Identifiant du club") @PathVariable clubId: UUID,
        @Parameter(description = "Identifiant du membre à nommer") @PathVariable targetUserId: UUID,
    ) = clubService.nameCoAdmin(clubId, userId(), targetUserId).toApiResponse()

    @Operation(
        summary = "Transférer l'administration",
        description = "Transmet le rôle d'administrateur du club à un autre membre (F-CLB-06)."
    )
    @PostMapping("/{clubId}/members/{targetUserId}/transfer-admin")
    fun transferAdmin(
        @Parameter(description = "Identifiant du club") @PathVariable clubId: UUID,
        @Parameter(description = "Identifiant du nouveau administrateur") @PathVariable targetUserId: UUID,
    ) = clubService.transferAdminRole(clubId, userId(), targetUserId).toApiResponse()

    @Operation(
        summary = "Quitter un club",
        description = "Le joueur quitte le club : il est retiré des effectifs engagés à venir et son historique est conservé (F-CLB-07)."
    )
    @DeleteMapping("/{clubId}/membership")
    fun leaveClub(
        @Parameter(description = "Identifiant du club à quitter") @PathVariable clubId: UUID
    ): ApiResponse {
        clubService.leaveClub(userId(), clubId)
        return ApiResponse.ok()
    }

    @Operation(
        summary = "Régénérer le code d'adhésion",
        description = "Invalide l'ancien code d'adhésion et en génère un nouveau (F-CLB-03)."
    )
    @PostMapping("/{clubId}/regenerate-code")
    fun regenerateCode(
        @Parameter(description = "Identifiant du club") @PathVariable clubId: UUID
    ) = clubService.regenerateCode(clubId, userId()).toApiResponse()

    @Operation(
        summary = "Modifier les paramètres du club",
        description = "Met à jour les informations et options du club ; réservé à l'administrateur (F-CLB-01)."
    )
    @PutMapping("/{clubId}/settings")
    fun updateSettings(
        @Parameter(description = "Identifiant du club") @PathVariable clubId: UUID,
        @RequestBody body: UpdateClubSettingsRequest,
    ) = clubService.updateSettings(clubId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
