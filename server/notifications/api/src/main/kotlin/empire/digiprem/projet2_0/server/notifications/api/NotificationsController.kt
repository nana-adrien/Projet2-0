package empire.digiprem.projet2_0.server.notifications.api

import empire.digiprem.projet2_0.contracts.notifications.NotificationDto
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.notifications.service.NotificationsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notifications", name = "Notifications")
@Tag(name = "Notifications", description = "Centre de notifications de l'utilisateur connecté")
class NotificationsController(
    private val notificationsService: NotificationsService,
) {

    @Operation(
        summary = "Lister mes notifications",
        description = "Historique des notifications de l'utilisateur connecté, de la plus récente à la plus ancienne (F-NOT-03)."
    )
    @GetMapping("/get")
    fun getNotifications(): List<NotificationDto> {
        return notificationsService.getNotifications(currentUserId())
    }

    @Operation(
        summary = "Marquer une notification comme lue",
        description = "Accuse la lecture d'une notification ; renvoie 204 sans contenu."
    )
    @PostMapping("/{id}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun markRead(
        @Parameter(description = "Identifiant de la notification") @PathVariable id: String
    ) {
        notificationsService.markRead(currentUserId(), id)
    }

    @Operation(
        summary = "Marquer toutes les notifications comme lues",
        description = "Accuse la lecture de toutes les notifications de l'utilisateur connecté ; renvoie 204 sans contenu."
    )
    @PostMapping("/read-all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun markAllRead() {
        notificationsService.markAllRead(currentUserId())
    }
}
