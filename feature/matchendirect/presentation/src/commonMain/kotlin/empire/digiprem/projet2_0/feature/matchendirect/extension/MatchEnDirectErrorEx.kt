package empire.digiprem.projet2_0.feature.matchendirect.extension

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.matchendirect.error.MatchEnDirectError

fun MatchEnDirectError.toUiText(): UiText = when (this) {
    MatchEnDirectError.NotFound   -> UiText.DynamicString("Introuvable.")
    MatchEnDirectError.Forbidden  -> UiText.DynamicString("Seul le marqueur désigné (ou son suppléant) peut faire ça.")
    MatchEnDirectError.Conflict   -> UiText.DynamicString("Action déjà effectuée.")
    MatchEnDirectError.InvalidData -> UiText.DynamicString("Informations invalides.")
    MatchEnDirectError.Unknown    -> UiText.DynamicString("Une erreur est survenue. Réessaie.")
}
