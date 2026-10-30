package empire.digiprem.projet2_0.feature.validation.config

import empire.digiprem.projet2_0.feature.validation.contester.ContesterViewModel
import empire.digiprem.projet2_0.feature.validation.datasource.IValidationDataSource
import empire.digiprem.projet2_0.feature.validation.datasource.ValidationKtorDataSource
import empire.digiprem.projet2_0.feature.validation.litige.LitigeViewModel
import empire.digiprem.projet2_0.feature.validation.repository.IValidationRepository
import empire.digiprem.projet2_0.feature.validation.repository.ValidationRepository
import empire.digiprem.projet2_0.feature.validation.resultat.ResultatViewModel
import empire.digiprem.projet2_0.feature.validation.usecase.ContesterResultatUseCase
import empire.digiprem.projet2_0.feature.validation.usecase.GetResultatUseCase
import empire.digiprem.projet2_0.feature.validation.usecase.RepondreContestationUseCase
import empire.digiprem.projet2_0.feature.validation.usecase.TrancherLitigeUseCase
import empire.digiprem.projet2_0.feature.validation.usecase.ValiderResultatUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinValidationModule = module {
    // Data layer
    singleOf(::ValidationKtorDataSource) bind IValidationDataSource::class
    singleOf(::ValidationRepository)     bind IValidationRepository::class

    // Domain — use cases
    singleOf(::GetResultatUseCase)
    singleOf(::ValiderResultatUseCase)
    singleOf(::ContesterResultatUseCase)
    singleOf(::RepondreContestationUseCase)
    singleOf(::TrancherLitigeUseCase)

    // Presentation — view models
    viewModelOf(::ResultatViewModel)
    viewModelOf(::ContesterViewModel)
    viewModelOf(::LitigeViewModel)
}
