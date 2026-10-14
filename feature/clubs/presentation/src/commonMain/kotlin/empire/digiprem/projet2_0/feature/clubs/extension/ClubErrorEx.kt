package empire.digiprem.projet2_0.feature.clubs.extension

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.clubs.error.ClubError

fun ClubError.toUiText(): UiText = when (this) {
    ClubError.NotFound    -> UiText.DynamicString("Introuvable.")
    ClubError.Forbidden   -> UiText.DynamicString("Tu n'as pas le droit de faire ça.")
    ClubError.Conflict    -> UiText.DynamicString("Tu es déjà membre de ce club ou ta demande est en attente.")
    ClubError.InvalidCode -> UiText.DynamicString("Ce code ne correspond à aucun club.")
    ClubError.Unknown     -> UiText.DynamicString("Une erreur est survenue. Réessaie.")
}
