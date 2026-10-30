package empire.digiprem.projet2_0.feature.amical.config

import empire.digiprem.projet2_0.feature.amical.creer.CreerAmicalViewModel
import empire.digiprem.projet2_0.feature.amical.datasource.AmicalKtorDataSource
import empire.digiprem.projet2_0.feature.amical.datasource.IAmicalDataSource
import empire.digiprem.projet2_0.feature.amical.repository.AmicalRepository
import empire.digiprem.projet2_0.feature.amical.repository.IAmicalRepository
import empire.digiprem.projet2_0.feature.amical.usecase.CreerMatchAmicalUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinAmicalModule = module {
    singleOf(::AmicalKtorDataSource) bind IAmicalDataSource::class
    singleOf(::AmicalRepository)     bind IAmicalRepository::class
    singleOf(::CreerMatchAmicalUseCase)
    viewModelOf(::CreerAmicalViewModel)
}
