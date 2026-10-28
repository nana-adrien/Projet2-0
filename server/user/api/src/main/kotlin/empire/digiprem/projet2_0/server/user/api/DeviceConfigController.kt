package empire.digiprem.projet2_0.server.user.api


import empire.digiprem.projet2_0.contracts.config.ConnectedDeviceResponse
import empire.digiprem.projet2_0.contracts.config.InitialDeviceConfigRequest
import empire.digiprem.projet2_0.contracts.config.UpdatePushTokenRequest
import empire.digiprem.projet2_0.server.common.dto.toApiResponse
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.common.util.toUUID
import empire.digiprem.projet2_0.server.user.service.DeviceConfigService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@RestController
@RequestMapping("/api/v1/device-config", name = "Configuration Appareil")
@Tag(name = "Device Configuration", description = "Gestion, synchronisation et révocation des appareils physiques des joueurs")
class DeviceConfigController(
    private val deviceConfigService: DeviceConfigService
) {

    @Operation(
        summary = "Afficher mes appareils connectés",
        description = "Récupère la liste de tous les terminaux actifs liés au compte du joueur connecté pour permettre la gestion multi-appareils (F-USER-12)."
    )
    @GetMapping("/my-devices")
    fun getMyConnectedDevices(): List<ConnectedDeviceResponse> {
        return deviceConfigService.getDevicesByUserId(UUID.fromString(currentUserId()))
    }


    @Operation(
        summary = "Synchroniser l'appareil (Post-connexion / Post-inscription)",
        description = "Enregistre ou met à jour l'ensemble du contexte technique de l'appareil actuel. Cette route applique la barrière de sécurité de 3 appareils maximum par compte."
    )
    @PutMapping("/sync")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun synchronizeDevice(
        @RequestBody request: InitialDeviceConfigRequest
    ) = deviceConfigService
        .synchronizeDevice(UUID.fromString(currentUserId()), request)
        .toApiResponse()


    @Operation(
        summary = "Mettre à jour uniquement le jeton de notification (FCM Token)",
        description = "Route  dédiée au rafraîchissement  du jeton de notification lorsque Firebase le réactualise en arrière-plan sur le téléphone."
    )
    @PutMapping("/update-token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePushToken(
        @RequestBody request: UpdatePushTokenRequest
    ) =        deviceConfigService
        .updateTokenOnly(currentUserId().toUUID(), request.deviceId, request.pushToken)
        .toApiResponse()

    @Operation(
        summary = "Révoquer / Déconnecter un appareil à distance",
        description = "Supprime la configuration matérielle et coupe définitivement le canal de notification push d'un appareil spécifique."
    )
    @DeleteMapping("/{deviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun revokeDevice(
        @Parameter(description = "Identifiant matériel unique de l'appareil à déconnecter") @PathVariable deviceId: String
    ) = deviceConfigService
        .revokeDevice(UUID.fromString(currentUserId()), deviceId)
        .toApiResponse()

}