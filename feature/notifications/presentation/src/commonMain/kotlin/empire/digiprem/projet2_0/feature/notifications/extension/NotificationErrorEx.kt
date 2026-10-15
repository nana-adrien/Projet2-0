package empire.digiprem.projet2_0.feature.notifications.extension

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.notifications.error.NotificationError

fun NotificationError.toUiText(): UiText = when (this) {
    NotificationError.NotFound    -> UiText.DynamicString("Introuvable.")
    NotificationError.Forbidden   -> UiText.DynamicString("Action non autorisée.")
    NotificationError.InvalidData -> UiText.DynamicString("Informations invalides.")
    NotificationError.Unknown     -> UiText.DynamicString("Une erreur est survenue. Réessaie.")
}
