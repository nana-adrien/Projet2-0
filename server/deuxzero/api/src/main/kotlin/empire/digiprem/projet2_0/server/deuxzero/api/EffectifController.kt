package empire.digiprem.projet2_0.server.deuxzero.api

import empire.digiprem.projet2_0.contracts.deuxzero.AddEffectifRequest
import empire.digiprem.projet2_0.contracts.deuxzero.TransferPlayerRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.deuxzero.service.EffectifService
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

@RestController
@RequestMapping("/api/v1/effectifs", name = "Effectifs")
@Tag(name = "Effectifs", description = "Composition des effectifs engagés et transferts entre camps")
class EffectifController(
    private val effectifService: EffectifService,
) {

    @Operation(
        summary = "Ajouter un membre à un effectif",
        description = "Ajoute un membre du club à l'effectif d'un camp. Refusé si le joueur figure déjà dans l'autre camp du même 2-0 (F-EFF-01, F-EFF-02, RG-06)."
    )
    @PostMapping("/camps/{campId}/membres")
    fun addMember(
        @Parameter(description = "Identifiant du camp") @PathVariable campId: UUID,
        @RequestBody body: AddEffectifRequest,
    ) = effectifService.addMember(campId, userId(), body).toApiResponse()

    @Operation(
        summary = "Lister l'effectif d'un camp",
        description = "Joueurs engagés dans le camp, avec leur numéro de maillot et leurs dates d'entrée et de sortie."
    )
    @GetMapping("/camps/{campId}/membres")
    fun listEffectif(
        @Parameter(description = "Identifiant du camp") @PathVariable campId: UUID
    ) = effectifService.listEffectif(campId).toApiResponse()

    @Operation(
        summary = "Transférer un joueur de camp",
        description = "Fait passer un joueur d'un camp à l'autre du même 2-0. Le transfert est historisé pour préserver les statistiques (F-EFF-04, RG-09)."
    )
    @PostMapping("/transfert")
    fun transferPlayer(@RequestBody body: TransferPlayerRequest) =
        effectifService.transferPlayer(userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
