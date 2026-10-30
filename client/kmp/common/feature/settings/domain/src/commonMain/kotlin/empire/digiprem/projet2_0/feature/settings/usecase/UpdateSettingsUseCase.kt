package empire.digiprem.projet2_0.feature.settings.usecase

import empire.digiprem.projet2_0.feature.settings.model.PDZSettings
import empire.digiprem.projet2_0.feature.settings.repository.ISettingsRepository

class UpdateSettingsUseCase(private val repository: ISettingsRepository) {
    suspend operator fun invoke(settings: PDZSettings) = repository.updateSettings(settings)
}
