package empire.digiprem.projet2_0.server.calendrier.api

import empire.digiprem.projet2_0.contracts.matchendirect.DesignerCompositionRequest
import empire.digiprem.projet2_0.contracts.matchendirect.DesignerMarqueurRequest
import empire.digiprem.projet2_0.server.calendrier.service.CompositionService
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/matchs", name = "Composition")
class CompositionController(
    private val compositionService: CompositionService,
) {

    @PostMapping("/{matchId}/composition")
    fun designerComposition(@PathVariable matchId: UUID, @RequestBody body: DesignerCompositionRequest) =
        compositionService.designerComposition(matchId, userId(), body).toApiResponse()

    @GetMapping("/{matchId}/composition")
    fun listComposition(@PathVariable matchId: UUID) =
        compositionService.listComposition(matchId).toApiResponse()

    @PostMapping("/{matchId}/marqueur")
    fun designerMarqueur(@PathVariable matchId: UUID, @RequestBody body: DesignerMarqueurRequest) =
        compositionService.designerMarqueur(matchId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
