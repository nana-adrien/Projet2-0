package empire.digiprem.projet2_0.feature.dashboard.config

import empire.digiprem.projet2_0.feature.dashboard.accueil.AccueilViewModel
import empire.digiprem.projet2_0.feature.dashboard.dashboard.DashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val koinDashboardModule = module {
    viewModelOf(::DashboardViewModel)
    viewModelOf(::AccueilViewModel)
}
