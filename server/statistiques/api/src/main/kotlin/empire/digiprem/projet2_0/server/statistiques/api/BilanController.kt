package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.statistiques.service.BilanService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU25 — clôture de saison et bilan (RG-17 : camp champion). */
@RestController
@RequestMapping("/api/v1/saisons/{saisonId}", name = "Bilan")
@Tag(name = "Bilan de saison", description = "Clôture d'une saison et synthèse de l'année")
class BilanController(
    private val bilanService: BilanService,
) {

    @Operation(
        summary = "Clôturer la saison et générer le bilan",
        description = "Fige les résultats, désigne le camp champion et produit le bilan de la saison (F-SAI-03, F-BIL-01, RG-17, RG-18)."
    )
    @PostMapping("/cloturer-et-bilan")
    fun cloturerEtBilan(
        @Parameter(description = "Identifiant de la saison") @PathVariable saisonId: UUID
    ) = bilanService.cloturerEtBilan(saisonId, userId()).toApiResponse()

    @Operation(
        summary = "Consulter le bilan de la saison",
        description = "Camp champion, récompenses individuelles, faits marquants et chiffres clés (F-BIL-01, F-BIL-03)."
    )
    @GetMapping("/bilan")
    fun bilan(
        @Parameter(description = "Identifiant de la saison") @PathVariable saisonId: UUID
    ) = bilanService.bilan(saisonId).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
