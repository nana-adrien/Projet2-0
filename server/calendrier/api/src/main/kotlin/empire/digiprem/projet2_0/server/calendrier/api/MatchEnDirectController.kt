package empire.digiprem.projet2_0.server.calendrier.api

import empire.digiprem.projet2_0.contracts.matchendirect.AjouterEvenementRequest
import empire.digiprem.projet2_0.contracts.matchendirect.PeriodeRequest
import empire.digiprem.projet2_0.server.calendrier.service.EvenementService
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

/** CU17 — Saisir les événements en direct. RG-11 appliquée dans [EvenementService]. */
@RestController
@RequestMapping("/api/v1/matchs", name = "MatchEnDirect")
@Tag(name = "Match en direct", description = "Saisie en direct du score et des événements par le marqueur")
class MatchEnDirectController(
    private val evenementService: EvenementService,
) {

    @Operation(
        summary = "Consulter le match en direct",
        description = "Score, chronomètre et chronologie des événements du match (F-LIV-07)."
    )
    @GetMapping("/{matchId}/en-direct")
    fun getMatchEnDirect(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = evenementService.getMatchEnDirect(matchId).toApiResponse()

    @Operation(
        summary = "Lister les événements",
        description = "Événements saisis pour le match, dans l'ordre chronologique."
    )
    @GetMapping("/{matchId}/evenements")
    fun listEvenements(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = evenementService.listEvenements(matchId).toApiResponse()

    @Operation(
        summary = "Donner le coup d'envoi",
        description = "Démarre le chronomètre de la première période et fait passer le match au statut EN_COURS (F-LIV-02)."
    )
    @PostMapping("/{matchId}/demarrer")
    fun demarrer(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = evenementService.demarrerMatch(matchId, userId()).toApiResponse()

    @Operation(
        summary = "Saisir un événement",
        description = "Enregistre un but, un but contre son camp, un penalty, un arrêt décisif, un carton ou un remplacement (F-LIV-03)."
    )
    @PostMapping("/{matchId}/evenements")
    fun ajouterEvenement(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID,
        @RequestBody body: AjouterEvenementRequest,
    ) = evenementService.ajouterEvenement(matchId, userId(), body).toApiResponse()

    @Operation(
        summary = "Annuler le dernier événement",
        description = "Corrige immédiatement une erreur de saisie en annulant le dernier événement enregistré (F-LIV-04)."
    )
    @PostMapping("/{matchId}/evenements/annuler-dernier")
    fun annulerDernierEvenement(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = evenementService.annulerDernierEvenement(matchId, userId()).toApiResponse()

    @Operation(
        summary = "Passer à la mi-temps",
        description = "Clôt la période en cours et enregistre la pause avant la seconde période (F-LIV-02)."
    )
    @PostMapping("/{matchId}/mi-temps")
    fun passerMiTemps(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID,
        @RequestBody body: PeriodeRequest,
    ) = evenementService.passerMiTemps(matchId, userId(), body).toApiResponse()

    @Operation(
        summary = "Terminer le match",
        description = "Clôt le temps réglementaire et bascule le match en attente de validation (F-LIV-02, F-VAL-01)."
    )
    @PostMapping("/{matchId}/terminer")
    fun terminer(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID,
        @RequestBody body: PeriodeRequest,
    ) = evenementService.terminerMatch(matchId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
