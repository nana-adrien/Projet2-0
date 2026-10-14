package empire.digiprem.projet2_0.feature.deuxzero.create_deuxzero

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZeroVisibilite

val JOURS_SEMAINE = listOf("LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI", "DIMANCHE")

/** E24 — Créer un 2-0, assistant en 5 étapes (CU8). */
data class CreateDeuxZeroState(
    val step: Int = 1,
    val nom: String = "",
    val lieu: String = "",
    val visibilite: DeuxZeroVisibilite = DeuxZeroVisibilite.PRIVEE,
    val estInterne: Boolean = false,
    val dureeMiTempsMinutes: Int = 45,
    val nombreMiTemps: Int = 2,
    val nombreMaxJoueursParCamp: Int = 9,
    val dureePauseMinutes: Int = 10,
    val joursHabituels: Set<String> = emptySet(),
    val heureHabituelle: String = "",
    val delaiValidationAutomatiqueHeures: Int = 24,
    val transfertsAutorisesParSaison: Int? = null,
    val libelleCampA: String = "Bleus",
    val libelleCampB: String = "Rouges",
    val libelleSaison: String = "",
    val dateDebutSaison: String = "",
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    // Résultat affiché après création quand le 2-0 n'est pas interne : il reste un camp à engager
    // par l'administrateur d'un autre club, via le code ci-dessous.
    val codeCampBToShare: String? = null,
    val createdCampA: Camp? = null,
) {
    val canContinue: Boolean
        get() = when (step) {
            1 -> nom.isNotBlank()
            5 -> libelleCampA.isNotBlank() && libelleCampB.isNotBlank() && libelleSaison.isNotBlank() && dateDebutSaison.isNotBlank()
            else -> true
        }
}
