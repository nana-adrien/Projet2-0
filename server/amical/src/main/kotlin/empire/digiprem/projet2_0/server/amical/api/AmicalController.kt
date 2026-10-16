package empire.digiprem.projet2_0.server.amical.api

import empire.digiprem.projet2_0.contracts.amical.CreerMatchAmicalRequest
import empire.digiprem.projet2_0.server.amical.service.AmicalService
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/clubs/{clubId}/matchs-amicaux", name = "MatchsAmicaux")
class AmicalController(
    private val amicalService: AmicalService,
) {

    @PostMapping
    fun creerMatchAmical(@PathVariable clubId: UUID, @RequestBody body: CreerMatchAmicalRequest) =
        amicalService.creerMatchAmical(clubId, UUID.fromString(currentUserId()), body).toApiResponse()
}
