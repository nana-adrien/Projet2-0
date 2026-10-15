package empire.digiprem.projet2_0.feature.notifications.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import empire.digiprem.projet2_0.feature.notifications.preferences.PreferenceRoot

private enum class NotificationsTabScreen { LISTE, PREFERENCES }

/** Onglet Notifications : liste (E-existant) + préférences par 2-0 (E27, CU26). */
@Composable
fun NotificationsTabRoot() {
    var screen by remember { mutableStateOf(NotificationsTabScreen.LISTE) }

    when (screen) {
        NotificationsTabScreen.LISTE -> NotificationsRoot(onPreferencesClick = { screen = NotificationsTabScreen.PREFERENCES })
        NotificationsTabScreen.PREFERENCES -> PreferenceRoot(onNavigateBack = { screen = NotificationsTabScreen.LISTE })
    }
}
