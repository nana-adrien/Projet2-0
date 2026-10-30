package empire.digiprem.projet2_0.feature.dashboard.dashboard

sealed interface DashboardEvent {
    data object OnLogout : DashboardEvent
}
