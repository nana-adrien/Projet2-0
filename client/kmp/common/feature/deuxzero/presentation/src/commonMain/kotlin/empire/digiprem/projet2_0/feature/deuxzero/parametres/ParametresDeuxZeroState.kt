package empire.digiprem.projet2_0.feature.deuxzero.parametres

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZeroVisibilite
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif

private const val STATUT_ARCHIVE = "ARCHIVE"

/** CU13 — Modifier les paramètres d'un 2-0 (F-DZ-08, F-DZ-09, F-DZ-10, F-DZ-11). Pas d'écran dédié
 * dans les maquettes : atteint depuis le bouton "Paramètres" du calendrier (E07, organisateur
 * uniquement). La modification ponctuelle de la durée de mi-temps pour un seul match (RG-10 alt)
 * reste gérée au niveau du match, pas ici. */
data class ParametresDeuxZeroState(
    val deuxZeroId: String = "",
    val statutActuel: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isTransferringOrganisateur: Boolean = false,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
    val joursHabituels: Set<String> = emptySet(),
    val heureHabituelle: String = "",
    val dureeMiTempsMinutes: String = "",
    val delaiValidationAutomatiqueHeures: String = "",
    val transfertsAutorisesParSaison: String = "",
    val visibilite: DeuxZeroVisibilite = DeuxZeroVisibilite.PRIVEE,
    val membres: List<Effectif> = emptyList(),
    val showTransferOrganisateurDialog: Boolean = false,
    val targetOrganisateur: Effectif? = null,
) {
    val estArchive: Boolean get() = statutActuel == STATUT_ARCHIVE
    val isFormValid: Boolean
        get() = dureeMiTempsMinutes.toIntOrNull() != null && delaiValidationAutomatiqueHeures.toIntOrNull() != null
}
