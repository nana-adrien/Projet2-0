package empire.digiprem.projet2_0.server.calendrier.api

import empire.digiprem.projet2_0.contracts.calendrier.AjouterMatchExceptionnelRequest
import empire.digiprem.projet2_0.contracts.calendrier.ModifierMatchRequest
import empire.digiprem.projet2_0.server.calendrier.service.CalendrierService
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/calendrier", name = "Calendrier")
class CalendrierController(
    private val calendrierService: CalendrierService,
) {

    @PostMapping("/{deuxZeroId}/generer")
    fun genererCalendrier(@PathVariable deuxZeroId: UUID) =
        calendrierService.genererCalendrier(deuxZeroId, userId()).toApiResponse()

    @PostMapping("/{deuxZeroId}/matchs")
    fun ajouterMatchExceptionnel(@PathVariable deuxZeroId: UUID, @RequestBody body: AjouterMatchExceptionnelRequest) =
        calendrierService.ajouterMatchExceptionnel(deuxZeroId, userId(), body).toApiResponse()

    @GetMapping("/{deuxZeroId}/matchs")
    fun listMatchs(@PathVariable deuxZeroId: UUID) =
        calendrierService.listMatchs(deuxZeroId).toApiResponse()

    @PutMapping("/matchs/{matchId}")
    fun modifierMatch(@PathVariable matchId: UUID, @RequestBody body: ModifierMatchRequest) =
        calendrierService.modifierMatch(matchId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
