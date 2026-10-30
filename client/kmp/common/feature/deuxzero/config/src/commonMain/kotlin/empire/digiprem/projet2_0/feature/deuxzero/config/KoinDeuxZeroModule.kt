package empire.digiprem.projet2_0.feature.deuxzero.config

import empire.digiprem.projet2_0.feature.deuxzero.composer_effectif.ComposerEffectifViewModel
import empire.digiprem.projet2_0.feature.deuxzero.create_deuxzero.CreateDeuxZeroViewModel
import empire.digiprem.projet2_0.feature.deuxzero.datasource.DeuxZeroKtorDataSource
import empire.digiprem.projet2_0.feature.deuxzero.datasource.IDeuxZeroDataSource
import empire.digiprem.projet2_0.feature.deuxzero.engager_club.EngagerClubViewModel
import empire.digiprem.projet2_0.feature.deuxzero.parametres.ParametresDeuxZeroViewModel
import empire.digiprem.projet2_0.feature.deuxzero.repartition.RepartitionViewModel
import empire.digiprem.projet2_0.feature.deuxzero.repository.DeuxZeroRepository
import empire.digiprem.projet2_0.feature.deuxzero.repository.IDeuxZeroRepository
import empire.digiprem.projet2_0.feature.deuxzero.usecase.AddEffectifUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.CloseSaisonUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.CreateDeuxZeroUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.EngageClubUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListCampsUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListEffectifUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListMesDeuxZeroUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListSaisonsUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.NommerCapitaineUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.OpenSaisonUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.TransferOrganisateurUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.TransferPlayerUseCase
import empire.digiprem.projet2_0.feature.deuxzero.usecase.UpdateDeuxZeroSettingsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinDeuxZeroModule = module {
    // Data layer
    singleOf(::DeuxZeroKtorDataSource) bind IDeuxZeroDataSource::class
    singleOf(::DeuxZeroRepository)     bind IDeuxZeroRepository::class

    // Domain — use cases
    singleOf(::CreateDeuxZeroUseCase)
    singleOf(::ListMesDeuxZeroUseCase)
    singleOf(::EngageClubUseCase)
    singleOf(::ListCampsUseCase)
    singleOf(::NommerCapitaineUseCase)
    singleOf(::UpdateDeuxZeroSettingsUseCase)
    singleOf(::TransferOrganisateurUseCase)
    singleOf(::AddEffectifUseCase)
    singleOf(::ListEffectifUseCase)
    singleOf(::TransferPlayerUseCase)
    singleOf(::ListSaisonsUseCase)
    singleOf(::OpenSaisonUseCase)
    singleOf(::CloseSaisonUseCase)

    // Presentation — view models
    viewModelOf(::CreateDeuxZeroViewModel)
    viewModelOf(::EngagerClubViewModel)
    viewModelOf(::RepartitionViewModel)
    viewModelOf(::ComposerEffectifViewModel)
    viewModelOf(::ParametresDeuxZeroViewModel)
}
