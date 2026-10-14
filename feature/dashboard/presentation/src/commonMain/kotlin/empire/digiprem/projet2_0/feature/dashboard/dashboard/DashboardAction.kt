package empire.digiprem.projet2_0.feature.dashboard.dashboard

sealed interface DashboardAction {
    data class OnDestinationChange(val destination: DashboardDestination) : DashboardAction
}
