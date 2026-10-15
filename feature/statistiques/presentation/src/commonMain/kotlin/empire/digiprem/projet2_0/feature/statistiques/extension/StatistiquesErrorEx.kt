package empire.digiprem.projet2_0.feature.statistiques.extension

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.statistiques.error.StatistiquesError

fun StatistiquesError.toUiText(): UiText = when (this) {
    StatistiquesError.NotFound    -> UiText.DynamicString("Introuvable.")
    StatistiquesError.Forbidden   -> UiText.DynamicString("Action non autorisée.")
    StatistiquesError.Conflict    -> UiText.DynamicString("Action déjà effectuée.")
    StatistiquesError.InvalidData -> UiText.DynamicString("Informations invalides.")
    StatistiquesError.Unknown     -> UiText.DynamicString("Une erreur est survenue. Réessaie.")
}
