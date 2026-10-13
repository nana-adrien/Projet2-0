package empire.digiprem.projet2_0.server.clubs.api

import empire.digiprem.projet2_0.contracts.club.CreateClubRequest
import empire.digiprem.projet2_0.contracts.club.JoinClubRequest
import empire.digiprem.projet2_0.contracts.club.UpdateClubSettingsRequest
import empire.digiprem.projet2_0.contracts.base.ApiResponse
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.clubs.service.ClubService
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

    @PostMapping
    fun createClub(@RequestBody body: CreateClubRequest) =
        clubService.createClub(userId(), body).toApiResponse()

    @GetMapping("/mine")
    fun listMyClubs() = clubService.listMyClubs(userId()).toApiResponse()


    @PostMapping("/join")
    fun joinClub(@RequestBody body: JoinClubRequest) =
        clubService.requestJoin(userId(), body).toApiResponse()

    @GetMapping("/{clubId}/members")
    fun listMembers(@PathVariable clubId: UUID) =
        clubService.listMembers(clubId, userId()).toApiResponse()

    @PostMapping("/{clubId}/members/{targetUserId}/accept")
    fun acceptMember(@PathVariable clubId: UUID, @PathVariable targetUserId: UUID) =
        clubService.respondToRequest(clubId, userId(), targetUserId, accept = true).toApiResponse()

    @PostMapping("/{clubId}/members/{targetUserId}/reject")
    fun rejectMember(@PathVariable clubId: UUID, @PathVariable targetUserId: UUID) =
        clubService.respondToRequest(clubId, userId(), targetUserId, accept = false).toApiResponse()

    @DeleteMapping("/{clubId}/members/{targetUserId}")
    fun removeMember(@PathVariable clubId: UUID, @PathVariable targetUserId: UUID): ApiResponse {
        clubService.removeMember(clubId, userId(), targetUserId)
        return ApiResponse.ok()
    }

    @PostMapping("/{clubId}/members/{targetUserId}/co-admin")
    fun nameCoAdmin(@PathVariable clubId: UUID, @PathVariable targetUserId: UUID) =
        clubService.nameCoAdmin(clubId, userId(), targetUserId).toApiResponse()

    @PostMapping("/{clubId}/members/{targetUserId}/transfer-admin")
    fun transferAdmin(@PathVariable clubId: UUID, @PathVariable targetUserId: UUID) =
        clubService.transferAdminRole(clubId, userId(), targetUserId).toApiResponse()

    @DeleteMapping("/{clubId}/membership")
    fun leaveClub(@PathVariable clubId: UUID): ApiResponse {
        clubService.leaveClub(userId(), clubId)
        return ApiResponse.ok()
    }

    @PostMapping("/{clubId}/regenerate-code")
    fun regenerateCode(@PathVariable clubId: UUID) =
        clubService.regenerateCode(clubId, userId()).toApiResponse()

    @PutMapping("/{clubId}/settings")
    fun updateSettings(@PathVariable clubId: UUID, @RequestBody body: UpdateClubSettingsRequest) =
        clubService.updateSettings(clubId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
