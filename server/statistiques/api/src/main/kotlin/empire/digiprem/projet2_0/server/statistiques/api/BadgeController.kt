package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.statistiques.service.BadgeAttributionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU24 (partie badges, F-REC-02). */
@RestController
@RequestMapping("/api/v1", name = "Badges")
@Tag(name = "Badges", description = "Attribution automatique et consultation des badges des joueurs")
class BadgeController(
    private val badgeAttributionService: BadgeAttributionService,
) {

    /** Déclenché manuellement en l'absence d'un hook sur l'officialisation du match (CU19/20). */
    @Operation(
        summary = "Calculer les badges d'un match",
        description = "Attribue automatiquement les badges aux joueurs à partir d'un match officiel : premier but, doublé, triplé, passes décisives, matchs joués, etc. (F-REC-02)."
    )
    @PostMapping("/matchs/{matchId}/badges/calculer")
    fun calculer(
        @Parameter(description = "Identifiant du match") @PathVariable matchId: UUID
    ) = badgeAttributionService.calculerBadges(matchId).toApiResponse()

    @Operation(
        summary = "Lister les badges d'un joueur",
        description = "Badges obtenus par le joueur, pour la vitrine de son profil (F-REC-03)."
    )
    @GetMapping("/utilisateurs/{userId}/badges")
    fun badgesUtilisateur(
        @Parameter(description = "Identifiant du joueur") @PathVariable userId: UUID
    ) = badgeAttributionService.getBadgesUtilisateur(userId).toApiResponse()
}
