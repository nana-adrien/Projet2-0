package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.statistiques.service.BilanService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU25 — clôture de saison et bilan (RG-17 : camp champion). */
@RestController
@RequestMapping("/api/v1/saisons/{saisonId}", name = "Bilan")
class BilanController(
    private val bilanService: BilanService,
) {

    @PostMapping("/cloturer-et-bilan")
    fun cloturerEtBilan(@PathVariable saisonId: UUID) =
        bilanService.cloturerEtBilan(saisonId, userId()).toApiResponse()

    @GetMapping("/bilan")
    fun bilan(@PathVariable saisonId: UUID) =
        bilanService.bilan(saisonId).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
