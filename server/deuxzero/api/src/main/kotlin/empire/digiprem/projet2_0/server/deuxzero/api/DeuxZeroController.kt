package empire.digiprem.projet2_0.server.deuxzero.api

import empire.digiprem.projet2_0.contracts.deuxzero.CreateDeuxZeroRequest
import empire.digiprem.projet2_0.contracts.deuxzero.EngageClubRequest
import empire.digiprem.projet2_0.contracts.deuxzero.NommerCapitaineRequest
import empire.digiprem.projet2_0.contracts.deuxzero.UpdateDeuxZeroSettingsRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.deuxzero.service.DeuxZeroService
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
@RequestMapping("/api/v1/deuxzero", name = "DeuxZero")
@Tag(name = "2-0", description = "Création, engagement des clubs et organisation d'un 2-0")
class DeuxZeroController(
    private val deuxZeroService: DeuxZeroService,
) {

    @Operation(
        summary = "Créer un 2-0",
        description = "Crée un 2-0 avec ses règles de jeu, ses jours et son heure habituels, son délai de validation, ses deux camps et sa première saison (F-DZ-01). L'appelant devient organisateur."
    )
    @PostMapping
    fun create(@RequestBody body: CreateDeuxZeroRequest) =
        deuxZeroService.createDeuxZero(userId(), body).toApiResponse()

    @Operation(
        summary = "Lister mes 2-0",
        description = "Renvoie les 2-0 auxquels l'utilisateur participe, pour le sélecteur de 2-0 (F-DZ-07)."
    )
    @GetMapping("/mine")
    fun listMesDeuxZero() = deuxZeroService.listMesDeuxZero(userId()).toApiResponse()

    @Operation(
        summary = "Engager un club dans un 2-0",
        description = "Rattache le club de l'appelant au camp correspondant au code d'engagement fourni (F-DZ-04)."
    )
    @PostMapping("/engage")
    fun engageClub(@RequestBody body: EngageClubRequest) =
        deuxZeroService.engageClub(userId(), body).toApiResponse()

    @Operation(
        summary = "Lister les camps",
        description = "Camps A et B du 2-0 avec leur club, leur code d'engagement et leurs capitaines."
    )
    @GetMapping("/{deuxZeroId}/camps")
    fun listCamps(
        @Parameter(description = "Identifiant du 2-0") @PathVariable deuxZeroId: UUID
    ) = deuxZeroService.listCamps(deuxZeroId).toApiResponse()

    @Operation(
        summary = "Nommer un capitaine",
        description = "Nomme le capitaine ou le vice-capitaine d'un camp (F-EFF-03)."
    )
    @PostMapping("/camps/{campId}/capitaine")
    fun nommerCapitaine(
        @Parameter(description = "Identifiant du camp") @PathVariable campId: UUID,
        @RequestBody body: NommerCapitaineRequest,
    ) = deuxZeroService.nommerCapitaine(campId, userId(), body).toApiResponse()

    @Operation(
        summary = "Modifier les paramètres d'un 2-0",
        description = "Réservé à l'organisateur ; les changements s'appliquent aux matchs à venir (F-DZ-08)."
    )
    @PutMapping("/{deuxZeroId}/settings")
    fun updateSettings(
        @Parameter(description = "Identifiant du 2-0") @PathVariable deuxZeroId: UUID,
        @RequestBody body: UpdateDeuxZeroSettingsRequest,
    ) = deuxZeroService.updateSettings(deuxZeroId, userId(), body).toApiResponse()

    @Operation(
        summary = "Transférer l'organisation",
        description = "Transmet le rôle d'organisateur du 2-0 à un autre utilisateur (F-DZ-09)."
    )
    @PostMapping("/{deuxZeroId}/transfer-organisateur/{targetUserId}")
    fun transferOrganisateur(
        @Parameter(description = "Identifiant du 2-0") @PathVariable deuxZeroId: UUID,
        @Parameter(description = "Identifiant du nouvel organisateur") @PathVariable targetUserId: UUID,
    ) = deuxZeroService.transferOrganisateur(deuxZeroId, userId(), targetUserId).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
