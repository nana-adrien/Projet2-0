package empire.digiprem.projet2_0.server.settings.api

import empire.digiprem.projet2_0.contracts.settings.SettingsDto
import empire.digiprem.projet2_0.contracts.settings.UpdateSettingsRequest
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.settings.service.SettingsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/settings", name = "Settings")
@Tag(name = "Paramètres", description = "Préférences d'application de l'utilisateur connecté")
class SettingsController(
    private val settingsService: SettingsService,
) {

    @Operation(
        summary = "Consulter mes paramètres",
        description = "Langue, thème et activation globale des notifications de l'utilisateur connecté."
    )
    @GetMapping("/get")
    fun getSettings(): SettingsDto {
        return settingsService.getSettings(currentUserId())
    }

    @Operation(
        summary = "Modifier mes paramètres",
        description = "Met à jour la langue, le thème et l'activation globale des notifications ; les champs absents restent inchangés."
    )
    @PutMapping("/update")
    fun updateSettings(@RequestBody request: UpdateSettingsRequest): SettingsDto {
        return settingsService.updateSettings(currentUserId(), request)
    }
}
