package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.statistiques.service.BilanService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU25 — palmarès d'un 2-0 (une entrée par saison clôturée). */
@RestController
@RequestMapping("/api/v1/deuxzero", name = "Palmares")
@Tag(name = "Palmarès", description = "Historique des champions et récompenses d'un 2-0")
class PalmaresController(
    private val bilanService: BilanService,
) {

    @Operation(
        summary = "Consulter le palmarès",
        description = "Liste des champions et récompenses, une entrée par saison clôturée (F-BIL-06)."
    )
    @GetMapping("/{deuxZeroId}/palmares")
    fun palmares(
        @Parameter(description = "Identifiant du 2-0") @PathVariable deuxZeroId: UUID
    ) = bilanService.palmares(deuxZeroId).toApiResponse()
}
