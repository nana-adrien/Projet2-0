package empire.digiprem.projet2_0.server.deuxzero.api

import empire.digiprem.projet2_0.contracts.deuxzero.CreateDeuxZeroRequest
import empire.digiprem.projet2_0.contracts.deuxzero.EngageClubRequest
import empire.digiprem.projet2_0.contracts.deuxzero.NommerCapitaineRequest
import empire.digiprem.projet2_0.contracts.deuxzero.UpdateDeuxZeroSettingsRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.deuxzero.service.DeuxZeroService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/deuxzero", name = "DeuxZero")
class DeuxZeroController(
    private val deuxZeroService: DeuxZeroService,
) {

    @PostMapping
    fun create(@RequestBody body: CreateDeuxZeroRequest) =
        deuxZeroService.createDeuxZero(userId(), body).toApiResponse()

    @PostMapping("/engage")
    fun engageClub(@RequestBody body: EngageClubRequest) =
        deuxZeroService.engageClub(userId(), body).toApiResponse()

    @GetMapping("/{deuxZeroId}/camps")
    fun listCamps(@PathVariable deuxZeroId: UUID) =
        deuxZeroService.listCamps(deuxZeroId).toApiResponse()

    @PostMapping("/camps/{campId}/capitaine")
    fun nommerCapitaine(@PathVariable campId: UUID, @RequestBody body: NommerCapitaineRequest) =
        deuxZeroService.nommerCapitaine(campId, userId(), body).toApiResponse()

    @PutMapping("/{deuxZeroId}/settings")
    fun updateSettings(@PathVariable deuxZeroId: UUID, @RequestBody body: UpdateDeuxZeroSettingsRequest) =
        deuxZeroService.updateSettings(deuxZeroId, userId(), body).toApiResponse()

    @PostMapping("/{deuxZeroId}/transfer-organisateur/{targetUserId}")
    fun transferOrganisateur(@PathVariable deuxZeroId: UUID, @PathVariable targetUserId: UUID) =
        deuxZeroService.transferOrganisateur(deuxZeroId, userId(), targetUserId).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
