package empire.digiprem.projet2_0.server.calendrier.api

import empire.digiprem.projet2_0.contracts.calendrier.AjouterMatchExceptionnelRequest
import empire.digiprem.projet2_0.contracts.calendrier.ModifierMatchRequest
import empire.digiprem.projet2_0.server.calendrier.service.CalendrierService
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/calendrier", name = "Calendrier")
@Tag(name = "Calendrier", description = "Génération du calendrier et gestion des matchs d'un 2-0")
class CalendrierController(
    private val calendrierService: CalendrierService,
) {

    @Operation(
        summary = "Générer le calendrier",
        description = "Crée automatiquement les matchs de la saison en cours selon les jours récurrents et l'heure habituelle du 2-0 (F-CAL-01)."
    )
    @PostMapping("/{deuxZeroId}/generer")
    fun genererCalendrier(
        @Parameter(description = "Identifiant du 2-0") @PathVariable deuxZeroId: UUID
    ) = calendrierService.genererCalendrier(deuxZeroId, userId()).toApiResponse()

    @Operation(
        summary = "Ajouter un match exceptionnel",
        description = "Ajoute un match hors calendrier récurrent (F-CAL-02)."
    )
    @PostMapping("/{deuxZeroId}/matchs")
    fun ajouterMatchExceptionnel(
        @Parameter(description = "Identifiant du 2-0") @PathVariable deuxZeroId: UUID,
        @RequestBody body: AjouterMatchExceptionnelRequest,
    ) = calendrierService.ajouterMatchExceptionnel(deuxZeroId, userId(), body).toApiResponse()

    @Operation(
        summary = "Lister les matchs",
        description = "Matchs du 2-0 avec leur date, leur statut et leur score."
    )
    @GetMapping("/{deuxZeroId}/matchs")
    fun listMatchs(
        @Parameter(description = "Identifiant du 2-0") @PathVariable deuxZeroId: UUID
    ) = calendrierService.listMatchs(deuxZeroId).toApiResponse()

    @Operation(
        summary = "Modifier un match",
        description = "Déplace ou annule un match (statut PROGRAMME, REPORTE ou ANNULE) avec un motif ; les membres du 2-0 sont notifiés (F-CAL-03)."
    )
    @PutMapping("/matchs/{matchId}")
    fun modifierMatch(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID,
        @RequestBody body: ModifierMatchRequest,
    ) = calendrierService.modifierMatch(matchId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
