package empire.digiprem.projet2_0.feature.amical.extension

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.amical.error.AmicalError

fun AmicalError.toUiText(): UiText = when (this) {
    AmicalError.Forbidden   -> UiText.DynamicString("Seul l'administrateur du club peut organiser un match amical.")
    AmicalError.InvalidData -> UiText.DynamicString("Informations invalides.")
    AmicalError.Unknown     -> UiText.DynamicString("Une erreur est survenue. Réessaie.")
}
