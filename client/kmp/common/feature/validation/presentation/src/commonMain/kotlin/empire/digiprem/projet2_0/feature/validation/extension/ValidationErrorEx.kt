package empire.digiprem.projet2_0.feature.validation.extension

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.validation.error.ValidationError

fun ValidationError.toUiText(): UiText = when (this) {
    ValidationError.NotFound    -> UiText.DynamicString("Introuvable.")
    ValidationError.Forbidden   -> UiText.DynamicString("Seul le capitaine ou le vice-capitaine du camp peut faire ça.")
    ValidationError.Conflict    -> UiText.DynamicString("Action déjà effectuée.")
    ValidationError.InvalidData -> UiText.DynamicString("Informations invalides.")
    ValidationError.Unknown     -> UiText.DynamicString("Une erreur est survenue. Réessaie.")
}
