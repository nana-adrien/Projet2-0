package empire.digiprem.projet2_0.server.calendrier.api

import empire.digiprem.projet2_0.contracts.litige.ContesterRequest
import empire.digiprem.projet2_0.contracts.litige.RepondreContestationRequest
import empire.digiprem.projet2_0.contracts.litige.TrancherLitigeRequest
import empire.digiprem.projet2_0.server.calendrier.service.ValidationService
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

/** CU19 — Valider/contester un résultat. CU20 — Arbitrer un litige. */
@RestController
@RequestMapping("/api/v1/matchs", name = "Validation")
@Tag(name = "Validation des résultats", description = "Validation, contestation et arbitrage des résultats de match")
class ValidationController(private val validationService: ValidationService) {

    @Operation(
        summary = "Consulter le récapitulatif à valider",
        description = "Score, buteurs, passeurs, chronologie et signatures déjà enregistrées (F-VAL-01)."
    )
    @GetMapping("/{matchId}/resultat-a-valider")
    fun getResultat(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = validationService.getResultat(matchId).toApiResponse()

    @Operation(
        summary = "Valider le résultat",
        description = "Signature électronique horodatée d'un camp. Quand les deux camps ont validé, le match devient officiel et les statistiques sont intégrées (F-VAL-02)."
    )
    @PostMapping("/{matchId}/valider")
    fun valider(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = validationService.valider(matchId, userId()).toApiResponse()

    @Operation(
        summary = "Contester le résultat",
        description = "Refus motivé accompagné d'une proposition de correction ; le camp adverse est notifié (F-VAL-04)."
    )
    @PostMapping("/{matchId}/contester")
    fun contester(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID,
        @RequestBody body: ContesterRequest,
    ) = validationService.contester(matchId, userId(), body).toApiResponse()

    @Operation(
        summary = "Répondre à une contestation",
        description = "Acceptation (le match est corrigé puis officialisé) ou refus (le match passe en litige) (F-VAL-04)."
    )
    @PostMapping("/{matchId}/repondre-contestation")
    fun repondreContestation(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID,
        @RequestBody body: RepondreContestationRequest,
    ) = validationService.repondreContestation(matchId, userId(), body).toApiResponse()

    @Operation(
        summary = "Trancher un litige",
        description = "Décision définitive de l'organisateur : résultat initial, résultat corrigé ou match annulé (F-VAL-05)."
    )
    @PostMapping("/{matchId}/trancher-litige")
    fun trancherLitige(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID,
        @RequestBody body: TrancherLitigeRequest,
    ) = validationService.trancherLitige(matchId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
