package empire.digiprem.projet2_0.feature.clubs.config

import empire.digiprem.projet2_0.feature.clubs.create_club.CreateClubViewModel
import empire.digiprem.projet2_0.feature.clubs.datasource.ClubKtorDataSource
import empire.digiprem.projet2_0.feature.clubs.datasource.IClubDataSource
import empire.digiprem.projet2_0.feature.clubs.gestion_club.GestionClubViewModel
import empire.digiprem.projet2_0.feature.clubs.join_club.JoinClubViewModel
import empire.digiprem.projet2_0.feature.clubs.mes_clubs.MesClubsViewModel
import empire.digiprem.projet2_0.feature.clubs.repository.ClubRepository
import empire.digiprem.projet2_0.feature.clubs.repository.IClubRepository
import empire.digiprem.projet2_0.feature.clubs.usecase.CreateClubUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.GetClubUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.JoinClubUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.LeaveClubUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.ListMembersUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.ListMyClubsUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.NameCoAdminUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.RegenerateCodeUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.RemoveMemberUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.RespondToRequestUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.TransferAdminUseCase
import empire.digiprem.projet2_0.feature.clubs.usecase.UpdateClubSettingsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinClubsModule = module {
    // Data layer
    singleOf(::ClubKtorDataSource) bind IClubDataSource::class
    singleOf(::ClubRepository)     bind IClubRepository::class

    // Domain — use cases
    singleOf(::CreateClubUseCase)
    singleOf(::JoinClubUseCase)
    singleOf(::ListMyClubsUseCase)
    singleOf(::GetClubUseCase)
    singleOf(::ListMembersUseCase)
    singleOf(::RespondToRequestUseCase)
    singleOf(::RemoveMemberUseCase)
    singleOf(::LeaveClubUseCase)
    singleOf(::NameCoAdminUseCase)
    singleOf(::TransferAdminUseCase)
    singleOf(::RegenerateCodeUseCase)
    singleOf(::UpdateClubSettingsUseCase)

    // Presentation — view models
    viewModelOf(::MesClubsViewModel)
    viewModelOf(::JoinClubViewModel)
    viewModelOf(::CreateClubViewModel)
    viewModelOf(::GestionClubViewModel)
}
