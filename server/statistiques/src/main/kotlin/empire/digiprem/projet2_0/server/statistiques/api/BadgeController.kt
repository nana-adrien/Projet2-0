package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.statistiques.service.BadgeAttributionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU24 (partie badges, F-REC-02). */
@RestController
@RequestMapping("/api/v1", name = "Badges")
class BadgeController(
    private val badgeAttributionService: BadgeAttributionService,
) {

    /** Déclenché manuellement en l'absence d'un hook sur l'officialisation du match (CU19/20). */
    @PostMapping("/matchs/{matchId}/badges/calculer")
    fun calculer(@PathVariable matchId: UUID) =
        badgeAttributionService.calculerBadges(matchId).toApiResponse()

    @GetMapping("/utilisateurs/{userId}/badges")
    fun badgesUtilisateur(@PathVariable userId: UUID) =
        badgeAttributionService.getBadgesUtilisateur(userId).toApiResponse()
}
