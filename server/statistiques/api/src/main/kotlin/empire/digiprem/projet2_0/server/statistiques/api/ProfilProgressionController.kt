package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.statistiques.service.ProfilProgressionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU23 — profil public et progression d'un joueur. */
@RestController
@RequestMapping("/api/v1/statistiques/utilisateurs", name = "ProfilProgression")
@Tag(name = "Profil et progression", description = "Profil public et courbe de progression d'un joueur")
class ProfilProgressionController(
    private val profilProgressionService: ProfilProgressionService,
) {

    @Operation(
        summary = "Consulter le profil public d'un joueur",
        description = "Carte joueur, statistiques de la saison, records et classement, éventuellement filtrés par 2-0 ou par club (F-PRO-02, F-PRO-04)."
    )
    @GetMapping("/{userId}")
    fun profil(
        @Parameter(description = "Identifiant du joueur") @PathVariable userId: UUID,
        @Parameter(description = "Filtre sur un 2-0 ; absent = tous les 2-0 du joueur") @RequestParam(required = false) deuxZeroId: UUID?,
        @Parameter(description = "Filtre sur un club ; absent = tous les clubs du joueur") @RequestParam(required = false) clubId: UUID?,
    ) = profilProgressionService.getProfilPublic(userId, viewerId(), deuxZeroId, clubId).toApiResponse()

    @Operation(
        summary = "Consulter la progression d'un joueur",
        description = "Points de progression des buts et des passes, regroupés par mois ou par saison (F-PRO-03)."
    )
    @GetMapping("/{userId}/progression")
    fun progression(
        @Parameter(description = "Identifiant du joueur") @PathVariable userId: UUID,
        @Parameter(description = "Filtre sur un 2-0 ; absent = tous les 2-0 du joueur") @RequestParam(required = false) deuxZeroId: UUID?,
        @Parameter(description = "Granularité des points : MOIS (défaut) ou SAISON") @RequestParam(defaultValue = "MOIS") granularite: String,
    ) = profilProgressionService.getProgression(userId, deuxZeroId, granularite).toApiResponse()

    private fun viewerId(): UUID = UUID.fromString(currentUserId())
}
