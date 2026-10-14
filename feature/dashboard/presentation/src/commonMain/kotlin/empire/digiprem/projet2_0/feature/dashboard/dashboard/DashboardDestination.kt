package empire.digiprem.projet2_0.feature.dashboard.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class DashboardDestination(
    val label: String,
    val icon : ImageVector,
) {
    Clubs(
        label = "Clubs",
        icon  = Icons.Default.Groups,
    ),
    Profile(
        label = "Profile",
        icon  = Icons.Default.Person,
    ),
    Notifications(
        label = "Notifications",
        icon  = Icons.Default.Notifications,
    ),
    Settings(
        label = "Settings",
        icon  = Icons.Default.Settings,
    ),
}
