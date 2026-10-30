package empire.digiprem.projet2_0.feature.deuxzero.model

enum class DeuxZeroVisibilite { PUBLIQUE, PRIVEE }

data class DeuxZero(
    val id: String,
    val nom: String,
    val lieu: String?,
    val visibilite: DeuxZeroVisibilite,
    val estInterne: Boolean,
    val joursHabituels: List<String>,
    val heureHabituelle: String?,
    val dureeMiTempsMinutes: Int,
    val nombreMiTemps: Int,
    val dureePauseMinutes: Int?,
    val nombreMaxJoueursParCamp: Int,
    val delaiValidationAutomatiqueHeures: Int,
    val transfertsAutorisesParSaison: Int?,
    val statut: String,
    val organisateurId: String,
)

/** CU8 — Paramètres saisis dans l'assistant de création (E24, 5 étapes). */
data class CreateDeuxZeroInput(
    val nom: String,
    val lieu: String?,
    val visibilite: DeuxZeroVisibilite,
    val estInterne: Boolean,
    val joursHabituels: List<String>,
    val heureHabituelle: String?,
    val dureeMiTempsMinutes: Int,
    val nombreMiTemps: Int,
    val dureePauseMinutes: Int?,
    val nombreMaxJoueursParCamp: Int,
    val delaiValidationAutomatiqueHeures: Int,
    val transfertsAutorisesParSaison: Int?,
    val libelleCampA: String,
    val libelleCampB: String,
    val libelleSaison: String,
    val dateDebutSaison: String,
)

/** CU13 — Modifier les paramètres d'un 2-0. Champs nuls = inchangés (mise à jour partielle). */
data class UpdateDeuxZeroSettingsInput(
    val dureeMiTempsMinutes: Int? = null,
    val joursHabituels: List<String>? = null,
    val heureHabituelle: String? = null,
    val delaiValidationAutomatiqueHeures: Int? = null,
    val transfertsAutorisesParSaison: Int? = null,
    val visibilite: DeuxZeroVisibilite? = null,
    val statut: String? = null,
)
