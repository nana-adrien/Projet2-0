package empire.digiprem.projet2_0.server.social.api

import empire.digiprem.projet2_0.contracts.social.SignalerRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.social.service.SocialService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/signalements", name = "Signalements")
@Tag(name = "Signalements", description = "Signalement des contenus et comportements inappropriés")
class SocialController(
    private val socialService: SocialService,
) {

    @Operation(
        summary = "Signaler un contenu",
        description = "Enregistre un signalement d'un contenu inapproprié pour modération (F-SOC-05)."
    )
    @PostMapping
    fun signaler(@RequestBody body: SignalerRequest) =
        socialService.signaler(UUID.fromString(currentUserId()), body).toApiResponse()
}
