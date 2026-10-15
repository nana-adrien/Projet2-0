package empire.digiprem.projet2_0.feature.statistiques.config

import empire.digiprem.projet2_0.feature.statistiques.bilan.BilanViewModel
import empire.digiprem.projet2_0.feature.statistiques.classement.ClassementViewModel
import empire.digiprem.projet2_0.feature.statistiques.datasource.IStatistiquesDataSource
import empire.digiprem.projet2_0.feature.statistiques.datasource.StatistiquesKtorDataSource
import empire.digiprem.projet2_0.feature.statistiques.profil.ProfilViewModel
import empire.digiprem.projet2_0.feature.statistiques.repository.IStatistiquesRepository
import empire.digiprem.projet2_0.feature.statistiques.repository.StatistiquesRepository
import empire.digiprem.projet2_0.feature.statistiques.tableau.TableauSaisonViewModel
import empire.digiprem.projet2_0.feature.statistiques.usecase.GetBilanSaisonUseCase
import empire.digiprem.projet2_0.feature.statistiques.usecase.GetClassementUseCase
import empire.digiprem.projet2_0.feature.statistiques.usecase.GetProfilJoueurUseCase
import empire.digiprem.projet2_0.feature.statistiques.usecase.GetTableauSaisonUseCase
import empire.digiprem.projet2_0.feature.statistiques.usecase.GetVoteHommeDuMatchUseCase
import empire.digiprem.projet2_0.feature.statistiques.usecase.VoterHommeDuMatchUseCase
import empire.digiprem.projet2_0.feature.statistiques.vote.VoteViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinStatistiquesModule = module {
    // Data layer
    singleOf(::StatistiquesKtorDataSource) bind IStatistiquesDataSource::class
    singleOf(::StatistiquesRepository)     bind IStatistiquesRepository::class

    // Domain — use cases
    singleOf(::GetTableauSaisonUseCase)
    singleOf(::GetClassementUseCase)
    singleOf(::GetProfilJoueurUseCase)
    singleOf(::GetVoteHommeDuMatchUseCase)
    singleOf(::VoterHommeDuMatchUseCase)
    singleOf(::GetBilanSaisonUseCase)

    // Presentation — view models
    viewModelOf(::TableauSaisonViewModel)
    viewModelOf(::ClassementViewModel)
    viewModelOf(::ProfilViewModel)
    viewModelOf(::VoteViewModel)
    viewModelOf(::BilanViewModel)
}
