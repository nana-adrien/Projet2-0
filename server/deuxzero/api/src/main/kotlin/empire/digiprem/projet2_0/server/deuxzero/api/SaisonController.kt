package empire.digiprem.projet2_0.server.deuxzero.api

import empire.digiprem.projet2_0.contracts.deuxzero.OpenSaisonRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.deuxzero.service.SaisonService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/deuxzero", name = "Saisons")
@Tag(name = "Saisons", description = "Ouverture, consultation et clôture des saisons d'un 2-0")
class SaisonController(
    private val saisonService: SaisonService,
) {

    @Operation(
        summary = "Lister les saisons",
        description = "Historique des saisons du 2-0, de la plus récente à la plus ancienne (F-SAI-04)."
    )
    @GetMapping("/{deuxZeroId}/saisons")
    fun listSaisons(
        @Parameter(description = "Identifiant du 2-0") @PathVariable deuxZeroId: UUID
    ) = saisonService.listSaisons(deuxZeroId).toApiResponse()

    @Operation(
        summary = "Ouvrir une saison",
        description = "Ouvre une nouvelle saison pour le 2-0 avec son libellé et ses dates ; réservé à l'organisateur (F-SAI-01)."
    )
    @PostMapping("/{deuxZeroId}/saisons")
    fun openSaison(
        @Parameter(description = "Identifiant du 2-0") @PathVariable deuxZeroId: UUID,
        @RequestBody body: OpenSaisonRequest,
    ) = saisonService.openSaison(deuxZeroId, userId(), body).toApiResponse()

    @Operation(
        summary = "Clôturer une saison",
        description = "Fige les résultats de la saison et désigne le camp champion (F-SAI-03, RG-17, RG-18)."
    )
    @PostMapping("/saisons/{saisonId}/cloturer")
    fun closeSaison(
        @Parameter(description = "Identifiant de la saison") @PathVariable saisonId: UUID
    ) = saisonService.closeSaison(saisonId, userId()).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
