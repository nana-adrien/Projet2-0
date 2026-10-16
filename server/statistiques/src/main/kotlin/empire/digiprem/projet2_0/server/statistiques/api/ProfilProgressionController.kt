package empire.digiprem.projet2_0.server.statistiques.api

import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.statistiques.service.ProfilProgressionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** CU23 — profil public et progression d'un joueur. */
@RestController
@RequestMapping("/api/v1/statistiques/utilisateurs", name = "ProfilProgression")
class ProfilProgressionController(
    private val profilProgressionService: ProfilProgressionService,
) {

    @GetMapping("/{userId}")
    fun profil(
        @PathVariable userId: UUID,
        @RequestParam(required = false) deuxZeroId: UUID?,
        @RequestParam(required = false) clubId: UUID?,
    ) = profilProgressionService.getProfilPublic(userId, viewerId(), deuxZeroId, clubId).toApiResponse()

    @GetMapping("/{userId}/progression")
    fun progression(
        @PathVariable userId: UUID,
        @RequestParam(required = false) deuxZeroId: UUID?,
        @RequestParam(defaultValue = "MOIS") granularite: String,
    ) = profilProgressionService.getProgression(userId, deuxZeroId, granularite).toApiResponse()

    private fun viewerId(): UUID = UUID.fromString(currentUserId())
}
