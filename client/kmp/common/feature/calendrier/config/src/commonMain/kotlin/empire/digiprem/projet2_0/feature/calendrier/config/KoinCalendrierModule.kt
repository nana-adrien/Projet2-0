package empire.digiprem.projet2_0.feature.calendrier.config

import empire.digiprem.projet2_0.feature.calendrier.calendrier.CalendrierViewModel
import empire.digiprem.projet2_0.feature.calendrier.datasource.CalendrierKtorDataSource
import empire.digiprem.projet2_0.feature.calendrier.datasource.ICalendrierDataSource
import empire.digiprem.projet2_0.feature.calendrier.fiche_match.FicheMatchViewModel
import empire.digiprem.projet2_0.feature.calendrier.repository.CalendrierRepository
import empire.digiprem.projet2_0.feature.calendrier.repository.ICalendrierRepository
import empire.digiprem.projet2_0.feature.calendrier.usecase.AjouterMatchExceptionnelUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.GenererCalendrierUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.GenererConvocationsUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.GetPresencesUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.ListConvocationsUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.ListMatchsUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.ModifierMatchUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.RelancerConvocationsUseCase
import empire.digiprem.projet2_0.feature.calendrier.usecase.RepondreConvocationUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinCalendrierModule = module {
    // Data layer
    singleOf(::CalendrierKtorDataSource) bind ICalendrierDataSource::class
    singleOf(::CalendrierRepository)     bind ICalendrierRepository::class

    // Domain — use cases
    singleOf(::GenererCalendrierUseCase)
    singleOf(::AjouterMatchExceptionnelUseCase)
    singleOf(::ListMatchsUseCase)
    singleOf(::ModifierMatchUseCase)
    singleOf(::GenererConvocationsUseCase)
    singleOf(::ListConvocationsUseCase)
    singleOf(::RepondreConvocationUseCase)
    singleOf(::RelancerConvocationsUseCase)
    singleOf(::GetPresencesUseCase)

    // Presentation — view models
    viewModelOf(::CalendrierViewModel)
    viewModelOf(::FicheMatchViewModel)
}
