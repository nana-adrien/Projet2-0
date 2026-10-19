package empire.digiprem.projet2_0.server.calendrier.api

import empire.digiprem.projet2_0.contracts.calendrier.RepondreConvocationRequest
import empire.digiprem.projet2_0.server.calendrier.service.ConvocationService
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
@RequestMapping("/api/v1/convocations", name = "Convocations")
@Tag(name = "Convocations", description = "Envoi des convocations, réponses des joueurs et suivi des présences")
class ConvocationController(
    private val convocationService: ConvocationService,
) {

    @Operation(
        summary = "Générer les convocations",
        description = "Envoie une convocation aux joueurs des deux effectifs pour le match (F-CAL-04)."
    )
    @PostMapping("/matchs/{matchId}/generer")
    fun genererConvocations(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = convocationService.genererConvocations(matchId, userId()).toApiResponse()

    @Operation(
        summary = "Lister les convocations",
        description = "Convocations du match avec la réponse de chaque joueur."
    )
    @GetMapping("/matchs/{matchId}")
    fun listConvocations(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = convocationService.listConvocations(matchId).toApiResponse()

    @Operation(
        summary = "Répondre à une convocation",
        description = "Enregistre la réponse du joueur convoqué : PRESENT, ABSENT ou INCERTAIN (F-CAL-04)."
    )
    @PostMapping("/{convocationId}/repondre")
    fun repondreConvocation(
        @Parameter(description = "Identifiant de la convocation") @PathVariable convocationId: UUID,
        @RequestBody body: RepondreConvocationRequest,
    ) = convocationService.repondreConvocation(convocationId, userId(), body).toApiResponse()

    @Operation(
        summary = "Relancer les joueurs sans réponse",
        description = "Envoie un rappel aux joueurs convoqués qui n'ont pas encore répondu (F-CAL-05)."
    )
    @PostMapping("/matchs/{matchId}/relancer")
    fun relancerConvocations(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = convocationService.relancerConvocations(matchId, userId()).toApiResponse()

    @Operation(
        summary = "Suivre les présences",
        description = "Compteur des présents, absents, incertains et sans réponse par camp, visible par tous les membres (F-CAL-06)."
    )
    @GetMapping("/matchs/{matchId}/presences")
    fun getPresences(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = convocationService.getPresences(matchId, userId()).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
