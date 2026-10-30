package empire.digiprem.projet2_0.feature.settings.usecase

import empire.digiprem.projet2_0.feature.settings.model.PDZSettings
import empire.digiprem.projet2_0.feature.settings.repository.ISettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(private val repository: ISettingsRepository) {
    operator fun invoke(): Flow<PDZSettings> = repository.observeSettings()
}
