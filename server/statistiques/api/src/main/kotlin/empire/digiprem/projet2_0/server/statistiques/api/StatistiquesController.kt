package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.statistiques.service.StatistiquesService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU22 — tableau de saison, classements, statistiques de club et confrontations (F-STA-01 à 07). */
@RestController
@RequestMapping("/api/v1/statistiques", name = "Statistiques")
@Tag(name = "Statistiques", description = "Tableaux de saison, classements, statistiques de club et confrontations")
class StatistiquesController(
    private val statistiquesService: StatistiquesService,
) {

    @Operation(
        summary = "Consulter le tableau d'une saison",
        description = "Score de l'année (victoires A, nuls, victoires B), buts, séries et records de la saison (F-STA-01, F-STA-04)."
    )
    @GetMapping("/saisons/{saisonId}/tableau")
    fun tableau(
        @Parameter(description = "Identifiant de la saison") @PathVariable saisonId: UUID
    ) = statistiquesService.getTableauSaison(saisonId).toApiResponse()

    @Operation(
        summary = "Consulter les classements individuels",
        description = "Meilleurs buteurs, passeurs et autres classements de la saison (F-STA-02)."
    )
    @GetMapping("/saisons/{saisonId}/classements")
    fun classements(
        @Parameter(description = "Identifiant de la saison") @PathVariable saisonId: UUID
    ) = statistiquesService.getClassements(saisonId).toApiResponse()

    @Operation(
        summary = "Consulter les statistiques d'un club",
        description = "Vue consolidée du club sur tous ses 2-0 et matchs amicaux, filtrable par période (F-STA-05, F-STA-07)."
    )
    @GetMapping("/clubs/{clubId}")
    fun statistiquesClub(
        @Parameter(description = "Identifiant du club") @PathVariable clubId: UUID,
        @Parameter(description = "Date de début incluse, au format ISO (AAAA-MM-JJ)") @RequestParam(required = false) dateDebut: String?,
        @Parameter(description = "Date de fin incluse, au format ISO (AAAA-MM-JJ)") @RequestParam(required = false) dateFin: String?,
    ) = statistiquesService.getStatistiquesClub(clubId, dateDebut, dateFin).toApiResponse()

    @Operation(
        summary = "Consulter les confrontations entre deux clubs",
        description = "Historique de toutes les rencontres entre deux clubs, filtrable par période (F-STA-06, F-STA-07)."
    )
    @GetMapping("/clubs/{clubAId}/confrontations/{clubBId}")
    fun confrontations(
        @Parameter(description = "Identifiant du premier club") @PathVariable clubAId: UUID,
        @Parameter(description = "Identifiant du second club") @PathVariable clubBId: UUID,
        @Parameter(description = "Date de début incluse, au format ISO (AAAA-MM-JJ)") @RequestParam(required = false) dateDebut: String?,
        @Parameter(description = "Date de fin incluse, au format ISO (AAAA-MM-JJ)") @RequestParam(required = false) dateFin: String?,
    ) = statistiquesService.getConfrontations(clubAId, clubBId, dateDebut, dateFin).toApiResponse()
}
