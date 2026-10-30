package empire.digiprem.projet2_0.feature.profile.config

import empire.digiprem.projet2_0.feature.profile.datasource.IProfileDataSource
import empire.digiprem.projet2_0.feature.profile.datasource.ProfileKtorDataSource
import empire.digiprem.projet2_0.feature.profile.profile.ProfileViewModel
import empire.digiprem.projet2_0.feature.profile.repository.IProfileRepository
import empire.digiprem.projet2_0.feature.profile.repository.ProfileRepository
import empire.digiprem.projet2_0.feature.profile.usecase.DeleteAccountUseCase
import empire.digiprem.projet2_0.feature.profile.usecase.GetProfileUseCase
import empire.digiprem.projet2_0.feature.profile.usecase.UpdateProfileUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinProfileModule = module {
    singleOf(::ProfileKtorDataSource) bind IProfileDataSource::class
    singleOf(::ProfileRepository)     bind IProfileRepository::class
    singleOf(::GetProfileUseCase)
    singleOf(::UpdateProfileUseCase)
    singleOf(::DeleteAccountUseCase)
    viewModelOf(::ProfileViewModel)
}
