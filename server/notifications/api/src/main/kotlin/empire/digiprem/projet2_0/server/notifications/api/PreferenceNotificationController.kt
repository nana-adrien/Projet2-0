package empire.digiprem.projet2_0.server.notifications.api

import empire.digiprem.projet2_0.contracts.notifications.SetPreferenceNotificationRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.notifications.service.PreferenceNotificationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/deuxzero/{deuxZeroId}/preference-notification", name = "PreferenceNotification")
@Tag(name = "Préférences de notification", description = "Niveau de notification de l'utilisateur pour un 2-0 donné")
class PreferenceNotificationController(
    private val preferenceNotificationService: PreferenceNotificationService,
) {

    @Operation(
        summary = "Consulter mes préférences",
        description = "Niveau de notification et mode « Ne pas déranger » pour ce 2-0 (F-NOT-02)."
    )
    @GetMapping
    fun getPreference(
        @Parameter(description = "Identifiant du 2-0") @PathVariable deuxZeroId: UUID
    ) = preferenceNotificationService.getPreference(deuxZeroId, userId()).toApiResponse()

    @Operation(
        summary = "Modifier mes préférences",
        description = "Fixe le niveau de notification (TOUT, BUTS_ET_SCORE_FINAL, ME_CONCERNANT ou AUCUNE) et le mode « Ne pas déranger » nocturne pour ce 2-0 (F-NOT-02)."
    )
    @PutMapping
    fun setPreference(
        @Parameter(description = "Identifiant du 2-0") @PathVariable deuxZeroId: UUID,
        @RequestBody body: SetPreferenceNotificationRequest,
    ) = preferenceNotificationService.setPreference(deuxZeroId, userId(), body).toApiResponse()

    private fun userId(): UUID = UUID.fromString(currentUserId())
}
