package empire.digiprem.projet2_0.server.settings.api

import empire.digiprem.projet2_0.contracts.settings.SettingsDto
import empire.digiprem.projet2_0.contracts.settings.UpdateSettingsRequest
import empire.digiprem.projet2_0.server.common.util.currentUserId
import empire.digiprem.projet2_0.server.settings.service.SettingsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/settings", name = "Settings")
class SettingsController(
    private val settingsService: SettingsService,
) {

    @GetMapping("/get")
    fun getSettings(): SettingsDto {
        return settingsService.getSettings(currentUserId())
    }

    @PutMapping("/update")
    fun updateSettings(@RequestBody request: UpdateSettingsRequest): SettingsDto {
        return settingsService.updateSettings(currentUserId(), request)
    }
}
