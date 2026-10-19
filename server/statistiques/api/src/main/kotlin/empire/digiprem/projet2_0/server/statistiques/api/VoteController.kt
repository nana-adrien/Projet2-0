package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.contracts.statistiques.VoteRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.statistiques.service.VoteService
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

/** CU24 — vote "homme du match". */
@RestController
@RequestMapping("/api/v1/matchs/{matchId}/votes", name = "Vote")
@Tag(name = "Homme du match", description = "Vote et résultat de l'homme du match")
class VoteController(
    private val voteService: VoteService,
) {

    @Operation(
        summary = "Voter pour l'homme du match",
        description = "Vote d'un joueur présent sur la feuille de match, pour un joueur de n'importe quel camp sauf lui-même ; un seul vote par joueur (F-REC-01, RG-16)."
    )
    @PostMapping
    fun voter(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID,
        @RequestBody body: VoteRequest,
    ) = voteService.voter(matchId, userId(), body).toApiResponse()

    @Operation(
        summary = "Consulter le résultat du vote",
        description = "Décompte des voix et joueur désigné homme du match ; en cas d'égalité, la distinction est partagée (F-REC-01)."
    )
    @GetMapping("/resultat")
    fun resultat(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = voteService.resultat(matchId).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
