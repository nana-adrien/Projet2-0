package empire.digiprem.projet2_0.server.calendrier.api

import empire.digiprem.projet2_0.contracts.matchendirect.DesignerCompositionRequest
import empire.digiprem.projet2_0.contracts.matchendirect.DesignerMarqueurRequest
import empire.digiprem.projet2_0.server.calendrier.service.CompositionService
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
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
@RequestMapping("/api/v1/matchs", name = "Composition")
@Tag(name = "Composition d'équipe", description = "Composition d'avant-match et désignation du marqueur")
class CompositionController(
    private val compositionService: CompositionService,
) {

    @Operation(
        summary = "Définir la composition",
        description = "Ajoute ou met à jour un joueur dans la composition du match (titulaire ou remplaçant, présent ou non). La composition reste ajustable jusqu'au coup d'envoi (F-CAL-07)."
    )
    @PostMapping("/{matchId}/composition")
    fun designerComposition(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID,
        @RequestBody body: DesignerCompositionRequest,
    ) = compositionService.designerComposition(matchId, userId(), body).toApiResponse()

    @Operation(
        summary = "Consulter la composition",
        description = "Titulaires et remplaçants désignés pour le match."
    )
    @GetMapping("/{matchId}/composition")
    fun listComposition(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = compositionService.listComposition(matchId).toApiResponse()

    @Operation(
        summary = "Désigner le marqueur",
        description = "Désigne le marqueur du match ou son suppléant ; réservé au capitaine ou à l'organisateur (F-LIV-01)."
    )
    @PostMapping("/{matchId}/marqueur")
    fun designerMarqueur(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID,
        @RequestBody body: DesignerMarqueurRequest,
    ) = compositionService.designerMarqueur(matchId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
