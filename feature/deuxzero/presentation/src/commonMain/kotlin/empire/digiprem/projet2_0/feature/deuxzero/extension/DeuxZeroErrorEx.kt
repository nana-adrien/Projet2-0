package empire.digiprem.projet2_0.feature.deuxzero.extension

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.deuxzero.error.DeuxZeroError

fun DeuxZeroError.toUiText(): UiText = when (this) {
    DeuxZeroError.NotFound    -> UiText.DynamicString("Introuvable.")
    DeuxZeroError.Forbidden   -> UiText.DynamicString("Tu n'as pas le droit de faire ça.")
    DeuxZeroError.Conflict    -> UiText.DynamicString("Ce camp est déjà engagé, ou ce joueur appartient déjà à l'autre camp.")
    DeuxZeroError.InvalidCode -> UiText.DynamicString("Ce code ne correspond à aucun camp.")
    DeuxZeroError.Unknown     -> UiText.DynamicString("Une erreur est survenue. Réessaie.")
}
