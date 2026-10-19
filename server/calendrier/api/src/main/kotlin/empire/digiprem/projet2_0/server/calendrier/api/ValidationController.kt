package empire.digiprem.projet2_0.server.calendrier.api

import empire.digiprem.projet2_0.contracts.litige.ContesterRequest
import empire.digiprem.projet2_0.contracts.litige.RepondreContestationRequest
import empire.digiprem.projet2_0.contracts.litige.TrancherLitigeRequest
import empire.digiprem.projet2_0.server.calendrier.service.ValidationService
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU19 — Valider/contester un résultat. CU20 — Arbitrer un litige. */
@RestController
@RequestMapping("/api/v1/matchs", name = "Validation")
class ValidationController(private val validationService: ValidationService) {

    @GetMapping("/{matchId}/resultat-a-valider")
    fun getResultat(@PathVariable matchId: UUID) = validationService.getResultat(matchId).toApiResponse()

    @PostMapping("/{matchId}/valider")
    fun valider(@PathVariable matchId: UUID) = validationService.valider(matchId, userId()).toApiResponse()

    @PostMapping("/{matchId}/contester")
    fun contester(@PathVariable matchId: UUID, @RequestBody body: ContesterRequest) =
        validationService.contester(matchId, userId(), body).toApiResponse()

    @PostMapping("/{matchId}/repondre-contestation")
    fun repondreContestation(@PathVariable matchId: UUID, @RequestBody body: RepondreContestationRequest) =
        validationService.repondreContestation(matchId, userId(), body).toApiResponse()

    @PostMapping("/{matchId}/trancher-litige")
    fun trancherLitige(@PathVariable matchId: UUID, @RequestBody body: TrancherLitigeRequest) =
        validationService.trancherLitige(matchId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
