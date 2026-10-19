package empire.digiprem.projet2_0.server.calendrier.api

import empire.digiprem.projet2_0.contracts.matchendirect.AjouterEvenementRequest
import empire.digiprem.projet2_0.contracts.matchendirect.PeriodeRequest
import empire.digiprem.projet2_0.server.calendrier.service.EvenementService
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU17 — Saisir les événements en direct. RG-11 appliquée dans [EvenementService]. */
@RestController
@RequestMapping("/api/v1/matchs", name = "MatchEnDirect")
class MatchEnDirectController(
    private val evenementService: EvenementService,
) {

    @GetMapping("/{matchId}/en-direct")
    fun getMatchEnDirect(@PathVariable matchId: UUID) =
        evenementService.getMatchEnDirect(matchId).toApiResponse()

    @GetMapping("/{matchId}/evenements")
    fun listEvenements(@PathVariable matchId: UUID) =
        evenementService.listEvenements(matchId).toApiResponse()

    @PostMapping("/{matchId}/demarrer")
    fun demarrer(@PathVariable matchId: UUID) =
        evenementService.demarrerMatch(matchId, userId()).toApiResponse()

    @PostMapping("/{matchId}/evenements")
    fun ajouterEvenement(@PathVariable matchId: UUID, @RequestBody body: AjouterEvenementRequest) =
        evenementService.ajouterEvenement(matchId, userId(), body).toApiResponse()

    @PostMapping("/{matchId}/evenements/annuler-dernier")
    fun annulerDernierEvenement(@PathVariable matchId: UUID) =
        evenementService.annulerDernierEvenement(matchId, userId()).toApiResponse()

    @PostMapping("/{matchId}/mi-temps")
    fun passerMiTemps(@PathVariable matchId: UUID, @RequestBody body: PeriodeRequest) =
        evenementService.passerMiTemps(matchId, userId(), body).toApiResponse()

    @PostMapping("/{matchId}/terminer")
    fun terminer(@PathVariable matchId: UUID, @RequestBody body: PeriodeRequest) =
        evenementService.terminerMatch(matchId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
