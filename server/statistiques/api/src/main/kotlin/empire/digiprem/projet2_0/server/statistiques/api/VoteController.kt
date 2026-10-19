package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.contracts.statistiques.VoteRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.statistiques.service.VoteService
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
class VoteController(
    private val voteService: VoteService,
) {

    @PostMapping
    fun voter(@PathVariable matchId: UUID, @RequestBody body: VoteRequest) =
        voteService.voter(matchId, userId(), body).toApiResponse()

    @GetMapping("/resultat")
    fun resultat(@PathVariable matchId: UUID) =
        voteService.resultat(matchId).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
