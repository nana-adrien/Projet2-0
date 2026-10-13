package empire.digiprem.projet2_0.feature.auth.extension

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.auth.error.AuthError

fun AuthError.toUiText(): UiText = when (this) {
    AuthError.InvalidCredentials -> UiText.DynamicString("Identifiant ou mot de passe incorrect.")
    AuthError.AlreadyExists      -> UiText.DynamicString("Ce pseudo ou ce contact est déjà utilisé.")
    AuthError.WeakPassword       -> UiText.DynamicString("Choisis un mot de passe d'au moins 8 caractères.")
    AuthError.InvalidCode        -> UiText.DynamicString("Ce code ne marche plus. Demande-en un nouveau.")
    AuthError.CodeExpired        -> UiText.DynamicString("Ce code a expiré. Demande-en un nouveau.")
    AuthError.TooManyAttempts    -> UiText.DynamicString("Trop d'essais. Réessaie dans 15 minutes.")
    AuthError.InvalidToken       -> UiText.DynamicString("La vérification a expiré, recommence depuis le début.")
    AuthError.Unknown            -> UiText.DynamicString("Une erreur est survenue. Réessaie.")
}
