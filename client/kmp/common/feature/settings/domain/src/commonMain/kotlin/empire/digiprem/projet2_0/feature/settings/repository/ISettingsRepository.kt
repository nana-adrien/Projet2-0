package empire.digiprem.projet2_0.feature.settings.repository

import empire.digiprem.projet2_0.feature.settings.model.PDZSettings
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    fun observeSettings(): Flow<PDZSettings>
    suspend fun updateSettings(settings: PDZSettings)
    suspend fun clearSettings()
}
