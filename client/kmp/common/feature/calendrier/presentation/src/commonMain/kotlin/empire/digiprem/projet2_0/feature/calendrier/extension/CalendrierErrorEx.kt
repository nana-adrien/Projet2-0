package empire.digiprem.projet2_0.feature.calendrier.extension

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.calendrier.error.CalendrierError

fun CalendrierError.toUiText(): UiText = when (this) {
    CalendrierError.NotFound   -> UiText.DynamicString("Introuvable.")
    CalendrierError.Forbidden  -> UiText.DynamicString("Seul l'organisateur, un capitaine ou un vice-capitaine peut faire ça.")
    CalendrierError.Conflict   -> UiText.DynamicString("Action déjà effectuée.")
    CalendrierError.InvalidData -> UiText.DynamicString("Informations invalides.")
    CalendrierError.Unknown    -> UiText.DynamicString("Une erreur est survenue. Réessaie.")
}
