package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.statistiques.service.StatistiquesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU22 — tableau de saison, classements, statistiques de club et confrontations (F-STA-01 à 07). */
@RestController
@RequestMapping("/api/v1/statistiques", name = "Statistiques")
class StatistiquesController(
    private val statistiquesService: StatistiquesService,
) {

    @GetMapping("/saisons/{saisonId}/tableau")
    fun tableau(@PathVariable saisonId: UUID) =
        statistiquesService.getTableauSaison(saisonId).toApiResponse()

    @GetMapping("/saisons/{saisonId}/classements")
    fun classements(@PathVariable saisonId: UUID) =
        statistiquesService.getClassements(saisonId).toApiResponse()

    @GetMapping("/clubs/{clubId}")
    fun statistiquesClub(
        @PathVariable clubId: UUID,
        @RequestParam(required = false) dateDebut: String?,
        @RequestParam(required = false) dateFin: String?,
    ) = statistiquesService.getStatistiquesClub(clubId, dateDebut, dateFin).toApiResponse()

    @GetMapping("/clubs/{clubAId}/confrontations/{clubBId}")
    fun confrontations(
        @PathVariable clubAId: UUID,
        @PathVariable clubBId: UUID,
        @RequestParam(required = false) dateDebut: String?,
        @RequestParam(required = false) dateFin: String?,
    ) = statistiquesService.getConfrontations(clubAId, clubBId, dateDebut, dateFin).toApiResponse()
}
