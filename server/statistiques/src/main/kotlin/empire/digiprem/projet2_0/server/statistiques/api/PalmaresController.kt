package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.statistiques.service.BilanService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU25 — palmarès d'un 2-0 (une entrée par saison clôturée). */
@RestController
@RequestMapping("/api/v1/deuxzero", name = "Palmares")
class PalmaresController(
    private val bilanService: BilanService,
) {

    @GetMapping("/{deuxZeroId}/palmares")
    fun palmares(@PathVariable deuxZeroId: UUID) =
        bilanService.palmares(deuxZeroId).toApiResponse()
}
