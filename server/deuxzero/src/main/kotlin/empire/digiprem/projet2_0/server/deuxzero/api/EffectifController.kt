package empire.digiprem.projet2_0.server.deuxzero.api

import empire.digiprem.projet2_0.contracts.deuxzero.AddEffectifRequest
import empire.digiprem.projet2_0.contracts.deuxzero.TransferPlayerRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.deuxzero.service.EffectifService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/effectifs", name = "Effectifs")
class EffectifController(
    private val effectifService: EffectifService,
) {

    @PostMapping("/camps/{campId}/membres")
    fun addMember(@PathVariable campId: UUID, @RequestBody body: AddEffectifRequest) =
        effectifService.addMember(campId, userId(), body).toApiResponse()

    @GetMapping("/camps/{campId}/membres")
    fun listEffectif(@PathVariable campId: UUID) =
        effectifService.listEffectif(campId).toApiResponse()

    @PostMapping("/transfert")
    fun transferPlayer(@RequestBody body: TransferPlayerRequest) =
        effectifService.transferPlayer(userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
