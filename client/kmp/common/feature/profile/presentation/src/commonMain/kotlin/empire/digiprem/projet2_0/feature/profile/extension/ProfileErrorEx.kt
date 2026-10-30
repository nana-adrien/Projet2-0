package empire.digiprem.projet2_0.feature.profile.extension

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.profile.error.ProfileError

fun ProfileError.toUiText(): UiText = when (this) {
    ProfileError.NotFound       -> UiText.DynamicString("Profil introuvable.")
    ProfileError.Forbidden      -> UiText.DynamicString("Action non autorisée.")
    ProfileError.PseudoDejaPris -> UiText.DynamicString("Ce pseudo est déjà pris par un autre compte.")
    ProfileError.Unknown        -> UiText.DynamicString("Une erreur est survenue. Réessaie.")
}
