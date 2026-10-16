package empire.digiprem.projet2_0.server.notifications.api

import empire.digiprem.projet2_0.contracts.notifications.SetPreferenceNotificationRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.notifications.service.PreferenceNotificationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/deuxzero/{deuxZeroId}/preference-notification", name = "PreferenceNotification")
class PreferenceNotificationController(
    private val preferenceNotificationService: PreferenceNotificationService,
) {

    @GetMapping
    fun getPreference(@PathVariable deuxZeroId: UUID) =
        preferenceNotificationService.getPreference(deuxZeroId, userId()).toApiResponse()

    @PutMapping
    fun setPreference(@PathVariable deuxZeroId: UUID, @RequestBody body: SetPreferenceNotificationRequest) =
        preferenceNotificationService.setPreference(deuxZeroId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
