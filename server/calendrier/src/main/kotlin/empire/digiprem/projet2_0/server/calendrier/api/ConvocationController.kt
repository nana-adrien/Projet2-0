package empire.digiprem.projet2_0.server.calendrier.api

import empire.digiprem.projet2_0.contracts.calendrier.RepondreConvocationRequest
import empire.digiprem.projet2_0.server.calendrier.service.ConvocationService
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
@RequestMapping("/api/v1/convocations", name = "Convocations")
class ConvocationController(
    private val convocationService: ConvocationService,
) {

    @PostMapping("/matchs/{matchId}/generer")
    fun genererConvocations(@PathVariable matchId: UUID) =
        convocationService.genererConvocations(matchId, userId()).toApiResponse()

    @GetMapping("/matchs/{matchId}")
    fun listConvocations(@PathVariable matchId: UUID) =
        convocationService.listConvocations(matchId).toApiResponse()

    @PostMapping("/{convocationId}/repondre")
    fun repondreConvocation(@PathVariable convocationId: UUID, @RequestBody body: RepondreConvocationRequest) =
        convocationService.repondreConvocation(convocationId, userId(), body).toApiResponse()

    @PostMapping("/matchs/{matchId}/relancer")
    fun relancerConvocations(@PathVariable matchId: UUID) =
        convocationService.relancerConvocations(matchId, userId()).toApiResponse()

    @GetMapping("/matchs/{matchId}/presences")
    fun getPresences(@PathVariable matchId: UUID) =
        convocationService.getPresences(matchId, userId()).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
