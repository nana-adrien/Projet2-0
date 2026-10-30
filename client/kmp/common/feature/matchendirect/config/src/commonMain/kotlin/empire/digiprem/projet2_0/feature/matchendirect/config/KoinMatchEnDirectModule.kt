package empire.digiprem.projet2_0.feature.matchendirect.config

import empire.digiprem.projet2_0.feature.matchendirect.composition.CompositionViewModel
import empire.digiprem.projet2_0.feature.matchendirect.datasource.IMatchEnDirectDataSource
import empire.digiprem.projet2_0.feature.matchendirect.datasource.MatchEnDirectKtorDataSource
import empire.digiprem.projet2_0.feature.matchendirect.marqueur.MarqueurViewModel
import empire.digiprem.projet2_0.feature.matchendirect.repository.IMatchEnDirectRepository
import empire.digiprem.projet2_0.feature.matchendirect.repository.MatchEnDirectRepository
import empire.digiprem.projet2_0.feature.matchendirect.suivre.SuivreEnDirectViewModel
import empire.digiprem.projet2_0.feature.matchendirect.usecase.AjouterEvenementUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.AnnulerDernierEvenementUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.DemarrerMatchUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.DesignerCompositionUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.DesignerMarqueurUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.GetMatchEnDirectUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.ListCompositionUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.ObserveMatchEnDirectUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.PasserMiTempsUseCase
import empire.digiprem.projet2_0.feature.matchendirect.usecase.TerminerMatchUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinMatchEnDirectModule = module {
    // Data layer
    singleOf(::MatchEnDirectKtorDataSource) bind IMatchEnDirectDataSource::class
    singleOf(::MatchEnDirectRepository)     bind IMatchEnDirectRepository::class

    // Domain — use cases
    singleOf(::DesignerCompositionUseCase)
    singleOf(::ListCompositionUseCase)
    singleOf(::DesignerMarqueurUseCase)
    singleOf(::DemarrerMatchUseCase)
    singleOf(::AjouterEvenementUseCase)
    singleOf(::AnnulerDernierEvenementUseCase)
    singleOf(::PasserMiTempsUseCase)
    singleOf(::TerminerMatchUseCase)
    singleOf(::GetMatchEnDirectUseCase)
    singleOf(::ObserveMatchEnDirectUseCase)

    // Presentation — view models
    viewModelOf(::CompositionViewModel)
    viewModelOf(::MarqueurViewModel)
    viewModelOf(::SuivreEnDirectViewModel)
}
