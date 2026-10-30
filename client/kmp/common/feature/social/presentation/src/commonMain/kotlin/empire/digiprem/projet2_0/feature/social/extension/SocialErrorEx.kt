package empire.digiprem.projet2_0.feature.social.extension

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.social.error.SocialError

fun SocialError.toUiText(): UiText = when (this) {
    SocialError.InvalidData -> UiText.DynamicString("Indique un motif.")
    SocialError.Unknown     -> UiText.DynamicString("Une erreur est survenue. Réessaie.")
}
