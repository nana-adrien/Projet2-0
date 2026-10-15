package empire.digiprem.projet2_0.feature.settings.config

import empire.digiprem.projet2_0.feature.settings.repository.ISettingsRepository
import empire.digiprem.projet2_0.feature.settings.repository.SettingsRepository
import empire.digiprem.projet2_0.feature.settings.settings.SettingsViewModel
import empire.digiprem.projet2_0.feature.settings.usecase.GetSettingsUseCase
import empire.digiprem.projet2_0.feature.settings.usecase.UpdateSettingsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinSettingsModule = module {
    singleOf(::SettingsRepository) bind ISettingsRepository::class
    singleOf(::GetSettingsUseCase)
    singleOf(::UpdateSettingsUseCase)
    viewModelOf(::SettingsViewModel)
}
