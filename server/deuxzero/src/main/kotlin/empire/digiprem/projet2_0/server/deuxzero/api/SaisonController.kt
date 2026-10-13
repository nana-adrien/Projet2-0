package empire.digiprem.projet2_0.server.deuxzero.api

import empire.digiprem.projet2_0.contracts.deuxzero.OpenSaisonRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.deuxzero.service.SaisonService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/deuxzero", name = "Saisons")
class SaisonController(
    private val saisonService: SaisonService,
) {

    @GetMapping("/{deuxZeroId}/saisons")
    fun listSaisons(@PathVariable deuxZeroId: UUID) =
        saisonService.listSaisons(deuxZeroId).toApiResponse()

    @PostMapping("/{deuxZeroId}/saisons")
    fun openSaison(@PathVariable deuxZeroId: UUID, @RequestBody body: OpenSaisonRequest) =
        saisonService.openSaison(deuxZeroId, userId(), body).toApiResponse()

    @PostMapping("/saisons/{saisonId}/cloturer")
    fun closeSaison(@PathVariable saisonId: UUID) =
        saisonService.closeSaison(saisonId, userId()).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
