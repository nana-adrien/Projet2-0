package empire.digiprem.projet2_0.server.social.api

import empire.digiprem.projet2_0.contracts.social.SignalerRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.social.service.SocialService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/signalements", name = "Signalements")
class SocialController(
    private val socialService: SocialService,
) {

    @PostMapping
    fun signaler(@RequestBody body: SignalerRequest) =
        socialService.signaler(UUID.fromString(currentUserId()), body).toApiResponse()
}
